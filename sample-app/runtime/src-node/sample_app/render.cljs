(ns sample-app.render
  (:require-macros [hypercrud.util.performance :as perf]
                   [hypercrud.util.template :as template])
  (:require [cljs.nodejs :as node]
            [hypercrud.client.core :as hc]
            [hypercrud.client.internal :as internal]
            [hypercrud.state.actions.core :as actions]
            [hypercrud.state.core :as state]
            [hypercrud.state.reducers :as reducers]
            [promesa.core :as p]
            [reagent.core :as reagent]
            [sample-app.core :as app]))


(def cheerio (node/require "cheerio"))

(def template (template/load-resource "index.html"))

(defn evaluated-template [static-resources state-val app-html]
  (let [$ (.load cheerio template)
        state-val (dissoc state-val :entry-uri)             ;remove any node specific values
        params {:root-conn-id hc/*root-conn-id*}]
    (-> ($ "#app-css") (.attr "href" (str static-resources "/styles.css")))
    (-> ($ "#root") (.html app-html))
    (-> ($ "#params") (.text (internal/transit-encode params)))
    (-> ($ "#state") (.text (internal/transit-encode state-val)))
    (-> ($ "#preamble") (.attr "src" (str static-resources "/preamble.js")))
    (-> ($ "#main") (.attr "src" (str static-resources "/main.js")))
    (.html $)))

(defn render-ui
  ([app-state peer static-resources]
   (render-ui app-state peer static-resources identity))
  ([app-state peer static-resources pre-state-change-f]
   (let [dispatch! #(assert false "dispatch! not supported in ssr")
         param-ctx {:dispatch! dispatch!
                    :peer peer
                    :root-db (hc/db peer hc/*root-conn-id* nil)}
         app-html (perf/time "render" (reagent/render-to-string (app/view app-state param-ctx)))]
     (perf/time "template" (evaluated-template static-resources ((or pre-state-change-f identity) @app-state) app-html)))))

(defn render-async-no-ssr [static-resources root-rel-path app-state peer]
  ; ignore root-rel-path so pushy kicks off the hydrating on the client
  (p/resolved (render-ui app-state peer static-resources)))

(defn render-async-ssr [static-resources root-rel-path app-state peer]
  (p/promise
    (fn [resolve! reject!]
      (let [reducer (fn [state-val action & args]
                      (let [state-val (apply reducers/root-reducer state-val action args)
                            ; we want pushy to kick-off and maintain our route on the client
                            pre-state-change-f #(dissoc % :route)]
                        ; we can still pre-render hydrate failures and other errors
                        (when (contains? #{:hydrate!-success :hydrate!-failure :set-error} action)
                          (resolve! (render-ui app-state peer static-resources pre-state-change-f)))
                        state-val))
            dispatch! (state/build-dispatch app-state reducer)
            param-ctx {:dispatch! dispatch!
                       :peer peer
                       :root-db (hc/db peer hc/*root-conn-id* nil)}]
        (binding [state/*request* #(app/request % param-ctx)]
          (dispatch! (actions/set-route-encoded root-rel-path app/index-link)))))))
