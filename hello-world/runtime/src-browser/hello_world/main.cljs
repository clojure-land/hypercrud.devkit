(ns hello-world.main
  (:require [goog.Uri]
            [hello-world.core :as app]
            [hypercrud.client.http :as http]
            [hypercrud.client.peer :as peer]
            [promesa.core :as p]
            [reagent.core :as reagent]))


(enable-console-print!)

(def state-atom (reagent/atom {}))

(def param-ctx {:peer (peer/->Peer state-atom)})

(defn mount-ui []
  (reagent/render [app/view state-atom param-ctx]
                  (.getElementById js/document "root")))

(defn hydrate-requests []
  (let [requests (app/request @state-atom param-ctx)]
    (-> (http/hydrate! (goog.Uri. "http://localhost:8080/api/") requests nil)
        (p/then (fn [hypercrud-response]
                  (swap! state-atom assoc :ptm (:pulled-trees-map hypercrud-response))))
        (p/catch (fn [e] (js/alert (pr-str e)))))))

(defn -main []
  (mount-ui)
  (hydrate-requests))
