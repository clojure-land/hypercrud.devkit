(ns sample-app.core
  (:require [hypercrud.browser.core :as browser]))


(def app-value {})

(def root-conn-id "todo")

(defn view [state-atom param-ctx]
  (let [{:keys [route]} @state-atom]
    [browser/safe-ui' route param-ctx]))

(defn request [state-value param-ctx]
  ; code your own data dependencies, or let the browser figure it out from an app-value
  (browser/request' (:route state-value) param-ctx))
