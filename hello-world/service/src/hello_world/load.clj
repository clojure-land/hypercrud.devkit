(ns hello-world.load
  (:require [clojure.java.io :as io]
            [datomic.api :as d]))


(defn load-db! [uri schema-file data-file]
  (let [conn (d/connect uri)]
    @(d/transact conn (-> schema-file slurp read-string))
    @(d/transact conn (-> data-file slurp read-string))))

(defn initialize-samples-blog [uri]
  (println "Loading samples-blog")

  (when-not (d/create-database uri)
    (throw (Error. "samples-blog db already exists")))

  (load-db! uri
            (io/resource "samples-blog/schema.edn")
            (io/resource "samples-blog/data.edn")))
