(ns simisola.events
  (:require
    [clojure.set :as set]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [re-frame.core :refer [reg-event-db reg-event-fx]]
    [simisola.db :as db]
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
    {:db (assoc-in db [:state :time] time)
     :dispatch [:change-view routes/feelings]}))

(reg-event-db
  :update-feeling
  (fn-traced [db [_ feeling]]
    (update-in db [:state :feelings]
               #(if (contains? % feeling)
                  (disj % feeling)
                  (conj % feeling)))))

(def practices
  [{:title "Morning Flow Yoga Meditation"
    :length [15 21]
    :attributes #{:fatique}
    :link "https://www.youtube.com/embed/oJ_7Le2n7fU"}
   {:title "Yoga For Tension Relief"
    :length [28 13]
    :attributes #{:tense}
    :link "https://www.youtube.com/embed/aKsu112bzHE"}])

(defn time->seconds [[minutes seconds]]
  (+ seconds (* 60 minutes)))

(defn practice-match? [{:keys [time feelings]} {:keys [length attributes]}]
  (and (> (time->seconds (if (number? time) [time 0] time))
          (time->seconds length))
       (set/subset? feelings attributes)))

(reg-event-fx
  :make-suggestion
  (fn-traced [{:keys [db]} _]
    {:db (assoc-in db
                   [:state :suggested-practice]
                   (-> (filter #(practice-match? (:state db) %) practices) first))
     :dispatch [:change-view routes/practice]}))
