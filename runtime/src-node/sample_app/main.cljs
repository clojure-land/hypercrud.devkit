(ns sample-app.main
  (:require [cljs.nodejs :as node]
            [hypercrud.client.peer :as peer]
            [hypercrud.state.reducers :as reducers]
            [hypercrud.types.URI :refer [->URI]]
            [hypercrud.util.core :as util]
            [promesa.core :as p]
            [reagent.core :as reagent]
            [sample-app.render :as render]
            [taoensso.timbre :as timbre]))


(enable-console-print!)
(timbre/set-level! :info)

(def express (node/require "express"))
(def st (node/require "st"))
(def winston (node/require "winston"))
(def expressWinston (node/require "express-winston"))

(def entry-uri (->URI "http://localhost:8081/api/"))

(def conf
  {:express-port "8080"
   :static-resources "/static"
   :browser-script-dir "../browser"
   :ssr-disabled false})

(defn ui-handler [req res]
  (let [root-rel-path (.-path req)
        render-fn (if (:ssr-disabled conf)
                    render/render-async-no-ssr
                    render/render-async-ssr)
        app-state (reagent/atom (-> {:entry-uri entry-uri}
                                    (reducers/root-reducer nil)))
        peer (peer/->Peer app-state)]
    (->
      (render-fn (:static-resources conf) root-rel-path app-state peer)
      (p/branch (fn [html]
                  (doto res
                    (.append "Cache-Control" "max-age=0")
                    (.status 200)
                    (.send html)))
                (fn [error]
                  (doto res
                    (.status 500)
                    (.send (str
                             "<html><body>"
                             "<h2>Error:</h2>"
                             "<pre>" (util/pprint-str error 100) "</pre>"
                             "</body></html>"))))))))

(defn start-express []
  (let [mount (st #js {:url (:static-resources conf)
                       :path (:browser-script-dir conf)})]
    (doto (express)
      (.use mount)                                          ; serve the browser client assets
      (.use (.logger expressWinston #js {"transports" #js [(new winston.transports.Console (clj->js {:json false :colorize true}))]}))
      (.get "/*" ui-handler)
      (.listen (:express-port conf)))
    (println (str "Express started on port: " (:express-port conf)))))

(defn ^:export main []
  (start-express))

(set! *main-cli-fn* (fn [] nil))
