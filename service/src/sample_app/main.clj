(ns sample-app.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [datomic.api :as d]
            [hypercrud.server.service :as service]
            [io.pedestal.http :as bootstrap]))

(defn edn! [resource-name] (-> resource-name io/resource slurp read-string))

(def service
  {::bootstrap/routes service/routes
   ::bootstrap/type :jetty
   ::bootstrap/port 8081
   ::bootstrap/join? false
   ::bootstrap/allowed-origins {:creds true :allowed-origins (constantly true)}})

(def app-db "datomic:free://localhost:4334/samples-blog")
(def fiddle-db "datomic:mem://samples-blog-fiddle")

(defn -main []
  (when (d/create-database app-db)
    (let [c (d/connect app-db)]
      (doseq [r ["samples-blog/schema.edn"
                 "samples-blog/data.edn"]]
        @(d/transact c (edn! r)))))

  (d/create-database fiddle-db)
  (let [c (d/connect fiddle-db)]
    (doseq [r ["source-code/schema.edn"
               "source-code/data.edn"]]
      @(d/transact c (edn! r))))

  (bootstrap/start (bootstrap/create-server service))

  (println "Serving"))
