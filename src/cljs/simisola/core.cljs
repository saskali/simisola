(ns simisola.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [simisola.config :as config]
   [simisola.events :as events]
   [simisola.subs]
   [simisola.views :as views]))


;; input: time availability and mood
;; get a suggestion
;; time availability can be < 20 min, < 40 min, < 60 min
;; mood can be fatigue, sad or tense

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
