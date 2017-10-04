(ns sample-app.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [datomic.api :as d]
            [hypercrud.server.service :as service]
            [io.pedestal.http :as bootstrap]))

(def service
  {::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8080
   ::bootstrap/allowed-origins {:creds true :allowed-origins (constantly true)}})

(defn load-db! [uri schema-file data-file]
  (let [conn (d/connect uri)]
    @(d/transact conn (-> schema-file slurp read-string))
    @(d/transact conn (-> data-file slurp read-string))))

(defn -main []
  (when (d/create-database "datomic:free://localhost:4334/samples-blog")
    (load-db! "datomic:free://localhost:4334/samples-blog"
              (io/resource "samples-blog/schema.edn")
              (io/resource "samples-blog/data.edn")))

  (d/create-database "datomic:mem://samples-blog-fiddle")
  (load-db! "datomic:mem://samples-blog-fiddle"
            (io/resource "source-code/schema.edn")
            (io/resource "source-code/data.edn"))

  (println "Starting pedestal")
  (bootstrap/start (bootstrap/create-server service)))
