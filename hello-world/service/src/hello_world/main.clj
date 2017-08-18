(ns hello-world.main
  (:gen-class)
  (:require [hello-world.load :as load]
            [hypercrud.server.datomic.core :as server]
            [hypercrud.server.service :as service]
            [io.pedestal.http :as bootstrap]))

(def service
  {::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8080
   ::bootstrap/allowed-origins {:creds true
                                :allowed-origins (constantly true)}})

(defn -main []
  (let [transactor-uri "datomic:mem://"]
    (load/initialize transactor-uri)

    (println "Initializing datomic")
    (server/init-datomic transactor-uri)

    (println "Starting pedestal")
    (bootstrap/start (bootstrap/create-server service))))
