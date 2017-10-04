(ns sample-app.load
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

(defn initialize-source-code-db [uri]
  (println "Loading source-code")

  (when-not (d/create-database uri)
    (throw (Error. "source-code db already exists")))

  (load-db! uri
            (io/resource "source-code/schema.edn")
            (io/resource "source-code/data.edn")))
