(ns hello-world.main
  (:gen-class)
  (:require [hello-world.load :as load]
            [hypercrud.server.service :as service]
            [io.pedestal.http :as bootstrap]))

(def service
  {::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8080
   ::bootstrap/allowed-origins {:creds true
                                :allowed-origins (constantly true)}})

(defn -main []
  (load/initialize-samples-blog "datomic:mem://samples-blog")

  (println "Starting pedestal")
  (bootstrap/start (bootstrap/create-server service)))
