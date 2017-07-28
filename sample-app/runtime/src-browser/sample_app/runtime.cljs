(ns sample-app.runtime
  (:require [goog.Uri]
            [hypercrud.browser.routing :as routing]
            [hypercrud.client.core :as hc]
            [hypercrud.client.peer :as peer]
            [hypercrud.state.actions.core :as actions]
            [hypercrud.state.core :as state]
            [hypercrud.state.reducers :as reducers]
            [hypercrud.ui.navigate-cmp :as navigate-cmp]
            [pushy.core :as pushy]
            [reagent.core :as reagent]

    ; defaults for defmethods
            [hypercrud.ui.auto-control-default]
            ))


(defn initialize-ui
  "Returns a react component wired with the appropriate dependencies set to mount a reactive ui

  Required arguments:
  root-conn-id    Number
  app-view        IFn[(state-atom ctx) => dom]
  app-request     IFn[(state-value ctx) => List[Request]]

  Optional arguments:
  root-reducer    IFn[(value action & args) => nil]
  initial-state   HashMap
  service-uri     URI
  "
  [root-conn-id app-view app-request
   & {:keys [root-reducer initial-state service-uri]
      :or {root-reducer reducers/root-reducer
           initial-state {}
           service-uri (goog.Uri. "http://localhost:8080/api/")}}]
  {:pre [root-conn-id app-view app-request]}
  (let [state-atom (reagent/atom (-> (merge {:entry-uri service-uri} initial-state)
                                     (root-reducer nil)))
        peer (peer/->Peer state-atom)
        dispatch! (state/build-dispatch state-atom root-reducer)
        history (pushy/pushy (fn [page-path]
                               (dispatch! (actions/set-route-encoded page-path)))
                             identity)
        param-ctx {:dispatch dispatch!
                   :peer peer
                   :root-db (hc/db peer root-conn-id nil)
                   :display-mode :user
                   :navigate-cmp navigate-cmp/navigate-cmp}]
    (set! hc/*root-conn-id* root-conn-id)
    (set! state/*request* #(app-request % param-ctx))
    (pushy/start! history)
    (reagent/create-class
      {:reagent-render (fn [] [app-view state-atom param-ctx])
       :component-did-mount
       (fn [this]
         (add-watch state-atom :browser-sync!
                    (fn [k r o n]
                      (when (not= (:route o) (:route n))
                        (pushy/set-token! history (routing/encode (:route n)))))))
       :component-will-unmount
       (fn [this]
         (remove-watch state-atom :browser-sync!))})))
