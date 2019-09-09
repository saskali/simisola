(ns simisola.events
  (:require
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :refer [reg-event-db]]
   [simisola.db :as db]))


(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))
