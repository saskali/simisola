(ns simisola.db
  (:require [clojure.set :as set]
            [simisola.practices :as practices]
            [simisola.routes :as routes]))


(def time-spans [20 40 60 80 100])

(defn practice-vals [key]
  (->> practices/library
       (map key)
       (apply set/union)))

(def default-db
  {:view          routes/time-span
   :practice-vals {:time-spans time-spans
                   :guided-by  (practice-vals :guided-by)
                   :types      (practice-vals :types)
                   :body-needs (practice-vals :body-needs)}
   :input         {:time       nil
                   :guided-by  #{}
                   :types      #{}
                   :body-needs #{}}})
