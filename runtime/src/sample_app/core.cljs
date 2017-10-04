(ns sample-app.core
  (:require [hypercrud.browser.core :as browser]
            [hypercrud.types.DbId :refer [->DbId]]
            [hypercrud.types.URI :refer [->URI]]
            [hypercrud.ui.navigate-cmp :as navigate-cmp]

    ; defaults for defmethods
            [hypercrud.ui.auto-control-default]))


(def domain
  {:domain/ident "samples-blog"
   :domain/databases #{{:dbhole/name "$"
                        :dbhole/uri (->URI "datomic:mem://samples-blog")}}
   :domain/code-databases #{{:dbhole/name "source-code"
                             :dbhole/uri (->URI "datomic:mem://source-code")}}})

(def index-route
  {:project "samples-blog"
   :code-database "source-code"
   :link-dbid (->DbId [:link/ident :samples/blog] (->URI "datomic:mem://source-code"))})

(def display-mode (atom :user))

(defn app-context [ctx]
  (assoc ctx
    :code-database "source-code"                            ; todo eventually can be removed
    :display-mode display-mode
    :domain domain
    :navigate-cmp navigate-cmp/navigate-cmp))

(defn view [state-atom ctx]
  (let [{:keys [error route]} @state-atom]
    (if error
      [:div [:h1 "Fatal error"]
       [:pre (pr-str error)]]
      [browser/ui-from-route route (app-context ctx)])))

(defn request [state-value ctx]
  ; code your own data dependencies, or let the browser figure it out from an source-code
  (browser/request-from-route (:route state-value) (app-context ctx)))
