(ns hello-world.main
    (:gen-class)
    (:require [hypercrud.server.datomic.core :as server]
      [hypercrud.server.service :as service]
      [io.pedestal.http :as bootstrap]))

(def service
  {:env :prod
   ::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8080})

(defn -main [transactor-uri]
      (assert transactor-uri "transactor-uri is a required command line arg")
      (server/init-datomic transactor-uri)
      (bootstrap/start (bootstrap/create-server service)))
