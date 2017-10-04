(ns sample-app.main
  (:gen-class)
  (:require [hypercrud.server.service :as service]
            [io.pedestal.http :as bootstrap]
            [sample-app.load :as load]))

(def service
  {::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8080
   ::bootstrap/allowed-origins {:creds true
                                :allowed-origins (constantly true)}})

(defn -main []
  (load/initialize-samples-blog "datomic:mem://samples-blog")
  (load/initialize-source-code-db "datomic:mem://source-code")

  (println "Starting pedestal")
  (bootstrap/start (bootstrap/create-server service)))
