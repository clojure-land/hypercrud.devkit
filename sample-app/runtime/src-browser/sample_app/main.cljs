(ns sample-app.main
  (:require [reagent.core :as reagent]
            [sample-app.core :as app]
            [sample-app.runtime :as runtime]))


(enable-console-print!)

(def ui
  (runtime/initialize-ui app/root-conn-id app/view app/request))

(defn mount-ui []
  (reagent/render [ui] (.getElementById js/document "root")))

(defn -main []
  (mount-ui))
