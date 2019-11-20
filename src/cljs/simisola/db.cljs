(ns simisola.db
  (:require [simisola.practices :as practices]
            [simisola.routes :as routes]))


(def time-spans [20 40 60])

(def feelings
  (->> practices/library
       (map :feelings)
       (apply clojure.set/union)))

(def default-db
  {:view       routes/time-span
   :time-spans time-spans
   :feelings   feelings
   :state      {:time-input nil
                :feelings-input #{}}})
