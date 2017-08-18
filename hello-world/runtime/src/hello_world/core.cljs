(ns hello-world.core
  (:require [cats.monad.either :as either]
            [hypercrud.client.core :as hc]
            [hypercrud.types.DbVal :refer [->DbVal]]
            [hypercrud.types.QueryRequest :refer [->QueryRequest]]))


(def request-blog
  (->QueryRequest '[:find ?post :in $ :where [?post :post/title]]
                  {"$" (->DbVal [:database/ident "samples-blog"] nil)}
                  {"?post" [(->DbVal [:database/ident "samples-blog"] nil) ['*]]}))

(defn request [state-value param-ctx]
  [request-blog])

(defn view [state-atom param-ctx]
  (-> (hc/hydrate (:peer param-ctx) request-blog)           ; synchronous and reactive
      (either/branch
        (fn [e] [:pre (pr-str e)])
        (fn [result]
          [:ul
           (->> result
                (map (fn [relation]
                       (let [post (get relation "?post")]
                         [:li {:key (:db/id post)}
                          (:post/title post)]))))]))))
