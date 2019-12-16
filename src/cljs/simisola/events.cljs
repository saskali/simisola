(ns simisola.events
  (:require
    [clojure.set :as set]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [re-frame.core :refer [reg-event-db reg-event-fx reg-fx]]
    [simisola.db :as db]
    [simisola.practices :as practices]
    [simisola.routes :as routes]))


(reg-event-db
 :initialize-db
 (fn-traced [_ _]
   db/default-db))

(reg-event-db
  :change-view
  (fn-traced [db [_ view-name]]
    (assoc db :view view-name)))

(reg-event-fx
  :update-time
  (fn-traced [{:keys [db]} [_ time]]
    {:db (assoc-in db [:state :time-input] time)
     :dispatch [:change-view routes/feelings]}))

(reg-event-db
  :update-feeling
  (fn-traced [db [_ feeling]]
    (update-in db [:state :feelings-input]
               #(if (contains? % feeling)
                  (disj % feeling)
                  (conj % feeling)))))

(reg-fx
  :open-suggestion
  (fn [practice-path]
    (.open js/window practice-path)))

(defn time->seconds [[minutes seconds]]
  (+ seconds (* 60 minutes)))

(defn practice-match? [{:keys [time-input feelings-input]} {:keys [length feelings]}]
  (and (> (time->seconds (if (number? time-input) [time-input 0] time-input))
          (time->seconds length))
       (set/subset? feelings-input feelings)))

(reg-event-fx
  :make-suggestion
  (fn-traced [{:keys [db]} _]
    (let [practice-suggestion (-> (filter #(practice-match? (:state db) %) practices/library) rand-nth)]
      {:db (assoc-in db [:state :suggested-practice] practice-suggestion)
       :open-suggestion (:path practice-suggestion)})))
