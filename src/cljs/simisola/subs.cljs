(ns simisola.subs
  (:require
   [re-frame.core :refer [reg-sub]]))


(reg-sub
  ::view
  (fn [db]
    (:view db)))

(reg-sub
  ::time-spans
  (fn [db]
    (:time-spans db)))

(reg-sub
  ::time-selected
  (fn [db]
    (get-in db [:state :time-selected])))

(reg-sub
  ::feelings
  (fn [db]
    (:feelings db)))

(reg-sub
 ::meditations
 (fn [db]
   (:meditations db)))
