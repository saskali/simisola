(ns simisola.subs
  (:require
   [re-frame.core :refer [reg-sub]]))


(reg-sub
  :view
  (fn [db]
    (:view db)))

(reg-sub
  :practice-vals
  (fn [db]
    (:practice-vals db)))

(reg-sub
  :input
  (fn [db _]
    (get db :input)))
