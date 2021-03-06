(ns simisola.db
  (:require [clojure.set :as set]
            [simisola.practices :as practices]
            [simisola.routes :as routes]))


(def max-time-span
  (->> practices/library
       (mapv (comp first :length))
       (apply max)))

(defn practice-vals [key]
  (->> practices/library
       (map key)
       (apply set/union)))

(def default-db
  {:view          routes/time-span
   :practice-vals {:time-span max-time-span
                   :guided-by  (practice-vals :guided-by)
                   :types      (practice-vals :types)
                   :body-needs (practice-vals :body-needs)}
   :input         {:time-span 0
                   :guided-by  #{}
                   :types      #{}
                   :body-needs #{}}})
