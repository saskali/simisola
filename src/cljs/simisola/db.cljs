(ns simisola.db
  (:require [simisola.routes :as routes]))


(def time-spans [20 40 60])

(def feelings #{:fatique :sad :tense})

(def default-db
  {:view        routes/time-span
   :time-spans  time-spans
   :feelings    feelings
   :state       {:time     nil
                 :feelings #{}}})
