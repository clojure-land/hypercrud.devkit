(ns sample-app.core
  (:require [hypercrud.browser.core :as browser]
            [hypercrud.types.DbId :refer [->DbId]]
            [hypercrud.ui.navigate-cmp :as navigate-cmp]

    ; defaults for defmethods
            [hypercrud.ui.auto-control-default]))


(def index-link
  {:domain nil
   :project "samples-blog"
   :link-dbid (->DbId [:link/ident :samples/blog] [:database/ident "root"])})

(defn app-context [ctx]
  (assoc ctx
    :display-mode :user
    :navigate-cmp navigate-cmp/navigate-cmp))

(defn view [state-atom param-ctx]
  (let [{:keys [error route]} @state-atom]
    (if error
      [:div [:h1 "Fatal error"]
       [:pre (pr-str error)]]
      [browser/safe-ui' route (app-context param-ctx)])))

(defn request [state-value param-ctx]
  ; code your own data dependencies, or let the browser figure it out from an app-value
  (browser/request' (:route state-value) (app-context param-ctx)))
