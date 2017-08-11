(def global-conf
  {:resource-paths #{"src" "resources"}
   :dependencies '[
                   [ch.qos.logback/logback-classic "1.1.7" :exclusions [org.slf4j/slf4j-api]]
                   [com.datomic/datomic-free "0.9.5561" :exclusions [org.slf4j/slf4j-nop]]
                   [com.hyperfiddle/hypercrud.server "0.2.0-SNAPSHOT"]
                   [io.pedestal/pedestal.jetty "0.5.1"]
                   [org.clojure/clojure "1.9.0-alpha14"]

                   ; build/test/dev
                   [sparkfund/boot-lein-generate "0.3.0" :scope "test"]
                   ]})

(->> (-> global-conf
         (assoc :boot.lein/project-clj global-conf))
     (mapcat identity)
     (apply set-env!))

(require 'boot.lein)

(task-options!
  pom {:project 'com.hyperfiddle/sample-app-service
       :version "0.0.1-SNAPSHOT"}
  aot {:namespace '#{sample-app.main}}
  jar {:main 'sample-app.main})

(deftask build []
         (comp (aot) (pom) (uber) (jar) (target)))

(when (> (.lastModified (clojure.java.io/file "build.boot"))
         (.lastModified (clojure.java.io/file "project.clj")))
  (boot.lein/write-project-clj))
