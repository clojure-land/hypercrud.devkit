(def global-conf
  {:source-paths #{"src"}
   :resource-paths #{"resources"}
   :dependencies '[[com.hyperfiddle/hypercrud.browser "0.1.0-SNAPSHOT"]
                   [funcool/promesa "1.8.1-HYPERCRUD"]
                   [kibu/pushy "0.3.6"]
                   [org.clojure/clojurescript "1.9.473"]
                   [reagent "0.6.0" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server]]


                   ; build/test/dev
                   [adzerk/boot-cljs "1.7.228-1" :scope "test"]
                   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                   [adzerk/boot-reload "0.5.1" :scope "test"]
                   [sparkfund/boot-lein-generate "0.3.0" :scope "test"]
                   [com.cemerick/piggieback "0.2.1" :scope "test"]
                   [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                   [org.clojure/clojure "1.8.0"]            ;; per boot-cljs-repl readme
                   [pandeiro/boot-http "0.7.3" :scope "test"]
                   [weasel "0.7.0" :scope "test"]]})

(apply set-env! (mapcat identity global-conf))
(set-env!
  ; we don't want intellij to look at util-src, just reference the dependency to auto-manage the module
  :boot.lein/project-clj {:source-paths (-> (:source-paths global-conf)
                                            (conj "src-node"
                                                  "src-browser"
                                                  "src-browser-dev"
                                                  "src-browser-prod"))})

(require '[adzerk.boot-cljs :refer :all]
         '[pandeiro.boot-http :refer :all]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         'boot.lein)

(task-options!
  pom {:project 'com.hyperfiddle/sample-app-runtime
       :version "0.1.0-SNAPSHOT"})


(deftask browser [b build VAL str "'dev' or 'prod'"]
         (assert (or (= build "dev") (= build "prod")))
         (merge-env! :source-paths #{"src-browser"
                                     (str "src-browser-" build)}
                     :resource-paths #{"generated-resources-browser"})
         (comp (cljs)
               (target :dir #{(str "target/browser")})))

(deftask browser-dev []
         (comp
           (watch)
           (speak)
           (browser :build "dev")))


(deftask node []
         (merge-env! :source-paths #{"src-node"}
                     :resource-paths #{"resources-node"})
         (comp (cljs)
               (target :dir #{"target/node"})))

(when (> (.lastModified (clojure.java.io/file "build.boot"))
         (.lastModified (clojure.java.io/file "project.clj")))
      (boot.lein/write-project-clj))
