(ns sample-app.render
  (:require-macros [hypercrud.util.template :as template])
  (:require [cljs.nodejs :as node]
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
        ;remove any node specific values
        state-val (dissoc state-val :entry-uri)]
    (-> ($ "#app-css") (.attr "href" (str static-resources "/styles.css")))
    (-> ($ "#root") (.html app-html))
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
                    :peer peer}
         app-html (reagent/render-to-string (app/view app-state param-ctx))]
     (evaluated-template static-resources ((or pre-state-change-f identity) @app-state) app-html))))

(defn render-async-no-ssr [static-resources root-rel-path app-state peer]
  ; ignore root-rel-path so pushy kicks off the hydrating on the client
  (p/resolved (render-ui app-state peer static-resources)))

(defn render-async-ssr [static-resources root-rel-path app-state peer]
  (p/promise
    (fn [resolve! reject!]
      (let [dispatch! (state/build-dispatch app-state reducers/root-reducer)
            param-ctx {:dispatch! dispatch!
                       :peer peer}]
        (add-watch app-state :ssr (fn [k r o n]
                                    (when (or (and (:hydrate-id o) (not (:hydrate-id n)))
                                              ; we can still pre-render hydrate failures and other errors
                                              (:error n))
                                      (try
                                        ; dissoc route so pushy kicks off and maintains the route on the client
                                        (resolve! (render-ui app-state peer static-resources #(dissoc % :route)))
                                        (catch :default e (reject! e)))
                                      (remove-watch r k))))
        (binding [state/*request* #(app/request % param-ctx)]
          (dispatch! (actions/set-route root-rel-path)))))))
