(ns sample-app.core
  (:require [hypercrud.browser.core :as browser]
            [hypercrud.types.DbId :refer [->DbId]]

    ; defaults for defmethods
            [hypercrud.ui.auto-control-default]))


(def index-link
  {:domain nil
   :project "samples-blog"
   :link-dbid (->DbId [:link/ident :samples/blog] [:database/ident "root"])})

(defn view [state-atom param-ctx]
  ; todo network error handling!!
  (let [{:keys [route]} @state-atom]
    [browser/safe-ui' route param-ctx]))

(defn request [state-value param-ctx]
  ; code your own data dependencies, or let the browser figure it out from an app-value
  (browser/request' (:route state-value) param-ctx))
