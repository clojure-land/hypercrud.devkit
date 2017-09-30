(ns hello-world.core
  (:require [hypercrud.client.core :as hc]
            [hypercrud.types.DbVal :refer [->DbVal]]
            [hypercrud.types.QueryRequest :refer [->QueryRequest]]
            [hypercrud.types.URI :refer [->URI]]))


(def request-blog
  (->QueryRequest '[:find ?post :in $ :where [?post :post/title]]
                  {"$" (->DbVal (->URI "datomic:mem://samples-blog") nil)}
                  {"?post" [(->DbVal(->URI "datomic:mem://samples-blog") nil) ['*]]}))

(defn request [state-value peer]
  [request-blog])

(defn view [state-atom peer]
  (let [result @(hc/hydrate peer request-blog)]             ; synchronous and reactive
    [:ul
     (->> result
          (map (fn [relation]
                 (let [post (get relation "?post")]
                   [:li {:key (:db/id post)}
                    (:post/title post)]))))]))
