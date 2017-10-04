(ns hello-world.load
  (:require [clojure.java.io :as io]
            [datomic.api :as d]))


(defn load-samples-blog! [uri]
  (let [schema (-> (io/resource "samples-blog/schema.edn") slurp read-string)
        data (-> (io/resource "samples-blog/data.edn") slurp read-string)
        conn (d/connect uri)]
    @(d/transact conn schema)
    @(d/transact conn data)))

(defn initialize-samples-blog [uri]
  (println "Loading samples-blog")

  (when-not (d/create-database uri)
    (throw (Error. "samples-blog db already exists")))

  (load-samples-blog! uri))
