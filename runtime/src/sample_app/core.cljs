(ns sample-app.core
  (:require [hypercrud.browser.core :as browser]
            [hypercrud.browser.routing :as routing]
            [hypercrud.types.DbId :refer [->DbId]]
            [hypercrud.types.URI :refer [->URI]]
            [hypercrud.ui.navigate-cmp :as navigate-cmp]
            [hypercrud.ui.auto-control-default]))


(def domain
  {:domain/ident "samples-blog"
   :domain/databases #{{:dbhole/name "$" :dbhole/uri (->URI "datomic:free://localhost:4334/samples-blog")}}
   :domain/code-databases #{{:dbhole/name "source-code" :dbhole/uri (->URI "datomic:mem://samples-blog-fiddle")}}})

(def index-route
  {:code-database "source-code"
   :link-dbid (->DbId [:link/ident :samples/blog] (->URI "datomic:mem://samples-blog-fiddle"))})

(def display-mode (atom :user))

(defn ui-error [e ctx]
  [:pre (:message e) "\n" (pr-str (:data e))])

(defn app-context [ctx]
  (assoc ctx
    :code-database "source-code"                            ; todo eventually can be removed
    :display-mode display-mode
    :domain domain
    :navigate-cmp navigate-cmp/navigate-cmp
    :ui-error ui-error))

(defn view [state-atom ctx]
  (let [{:keys [error encoded-route]} @state-atom
        route (or (routing/decode encoded-route) index-route)]
    (if error
      [:div [:h1 "Fatal error"]
       [:pre (pr-str error)]]
      [browser/ui-from-route route (app-context ctx)])))

(defn request [state-value ctx]
  (let [route (or (routing/decode (:encoded-route state-value)) index-route)]
    ; code your own data dependencies, or let the browser figure it out from an source-code
    (browser/request-from-route route (app-context ctx))))
