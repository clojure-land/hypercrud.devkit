(def global-conf
  {:resource-paths #{"src" "dev" "resources"}
   :dependencies '[
                   [com.datomic/datomic-free "0.9.5561" :exclusions [org.slf4j/slf4j-nop]]
                   [com.hyperfiddle/hypercrud.server "0.2.0-SNAPSHOT"]
                   [com.hyperfiddle/util "0.0.1-SNAPSHOT"]
                   [io.pedestal/pedestal.service "0.5.1"]
                   [ns-tracker "0.3.0"]
                   [org.clojure/clojure "1.9.0-alpha14"]

                   ; unchecked
                   [ch.qos.logback/logback-classic "1.1.7" :exclusions [org.slf4j/slf4j-api]]
                   [io.pedestal/pedestal.jetty "0.5.1"]
                   [org.clojure/core.async "0.2.395"]       ; transitive - override pedestal
                   [org.clojure/core.incubator "0.1.4"]     ; transitive - override pedestal
                   [org.clojure/tools.analyzer.jvm "0.6.10"] ; transitive - override core.match

                   ; build/test/dev
                   [sparkfund/boot-lein-generate "0.3.0" :scope "test"]]})

(->> (-> global-conf
         (assoc :boot.lein/project-clj {:dependencies (:dependencies global-conf)}))
     (mapcat identity)
     (apply set-env!))

(require 'boot.lein)


(task-options!
  pom {:project 'com.hyperfiddle/sample-app-service
       :version "0.0.1-SNAPSHOT"}
  aot {:namespace '#{service.main}}
  jar {:main 'service.main})

(deftask build []
         (comp (aot) (pom) (uber) (jar) (target)))

(when (> (.lastModified (clojure.java.io/file "build.boot"))
         (.lastModified (clojure.java.io/file "project.clj")))
  (boot.lein/write-project-clj))
