(ns hello-world.main
  (:require [hello-world.core :as app]
            [hypercrud.client.http :as http]
            [hypercrud.client.peer :as peer]
            [hypercrud.types.DbError :refer [DbError]]
            [hypercrud.types.URI :refer [->URI]]
            [hypercrud.util.core :refer [pprint-str]]
            [promesa.core :as p]
            [reagent.core :as reagent]))


(enable-console-print!)

(def state-atom (reagent/atom {:loading? true}))
(def peer (peer/->Peer state-atom))

(defn ui []
  (let [{:keys [http-error loading? ptm]} @state-atom]
    (cond
      loading?
      [:h2 "Loading..."]

      http-error
      [:div
       [:h2 "Network error"]
       [:pre (pprint-str http-error)]]

      (some #(instance? DbError %) (vals ptm))
      [:div
       [:h2 "Errors"]
       [:ul
        (->> ptm
             (filter (fn [[req resp]] (instance? DbError resp)))
             (map (fn [[req error]]
                    (let [error (peer/human-error error req)]
                      [:li {:key (hash req)}
                       [:p (:message error)]
                       [:pre (pprint-str req)]
                       [:pre (pprint-str (:data error))]]))))]]

      :else
      [app/view state-atom peer])))

(defn mount-ui []
  (reagent/render [ui] (.getElementById js/document "root")))

(defn hydrate-requests []
  (let [requests (app/request @state-atom peer)]
    (-> (http/hydrate! (->URI "http://localhost:8080/api/") requests nil)
        (p/then (fn [hypercrud-response]
                  (let [ptm (:pulled-trees-map hypercrud-response)]
                    (swap! state-atom assoc :ptm ptm :loading? false))))
        (p/catch #(swap! state-atom assoc :http-error % :loading? false)))))

(defn -main []
  (mount-ui)
  (hydrate-requests))
