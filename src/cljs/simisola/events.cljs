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
    {:db       (assoc-in db [:input :time] time)
     :dispatch [:change-view routes/body-needs]}))

(reg-event-db
  :update-values
  (fn-traced [db [_ value]]
    (update-in db [:input (:view db)]
               #(if (contains? % value)
                  (disj % value)
                  (conj % value)))))

(reg-fx
  :open-suggestion
  (fn [practice-path]
    (.open js/window practice-path)))

(defn time->seconds [[minutes seconds]]
  (+ seconds (* 60 minutes)))

(defn practice-match? [input practice]
  (let [time (:time input)]
    (and (> (time->seconds (if (number? time) [time 0] time))
            (time->seconds (:time practice)))
         (set/subset? (:body-needs input) (:body-needs practice))
         (set/subset? (:types input) (:types practice))
         (set/subset? (:guided-by practice) (:guided-by input)))))

(reg-event-fx
  :make-suggestion
  (fn-traced [{:keys [db]} _]
    (let [practice-suggestion (-> (filter #(practice-match? (:input db) %) practices/library) rand-nth)]
      {:db (assoc-in db [:input :suggested-practice] practice-suggestion)
       :open-suggestion (:path practice-suggestion)
       :dispatch [:change-view routes/practice]})))
