(ns simisola.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [re-frame.core :refer [reg-event-db reg-event-fx]]
    [simisola.db :as db]
    [simisola.routes :as routes]))


(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(reg-event-db
  ::change-view
  (fn-traced [db [_ view-name]]
    (assoc db :view view-name)))

(reg-event-fx
  ::update-time
  (fn-traced [{:keys [db]} [_ time]]
    {:db (assoc-in db [:state :time-selected] time)
     :dispatch [::change-view routes/feelings]}))

(reg-event-db
  ::update-feeling
  (fn-traced [db [_ feeling]]
    (update-in db [:state :feelings-selected]
               #(if (contains? % feeling)
                  (disj % feeling)
                  (conj % feeling)))))
