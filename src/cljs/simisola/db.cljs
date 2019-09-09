(ns simisola.db
  (:require [simisola.routes :as routes]))


(def time-spans [20 40 60])

(def feelings #{:fatique :sad :tense})

(def types #{:meditation :movement :magic :ritual :practice})

(def feelings-needs-met #{:affectionate :engaged :hopeful :confident
                          :excited :grateful :inspired :joyful
                          :exhilarated :peaceful :refreshed})

(def feelings-needs-not-met #{:afraid :annoyed :aversion :confused
                              :disconnected :disquiet :embarrassed
                              :fatigue :pain :sad :tense :vulnerable
                              :yearning})

(def meditations
  [{:id 1
     :time-span 20
    :feelings #{:fatigue}
    :content "URL1"}
   {:id 2
    :time-span 40
    :feelings #{:sad}
    :content "URL2"}
   {:id 3
    :time-span 20
    :feelings #{:fatique}
    :content "URL3"}
   {:id 4
    :time-span 20
    :feelings #{:fatique :tense}
    :content "URL4"}])

(def default-db
  {:view        routes/time-span
   :time-spans  time-spans
   :feelings    feelings
   :meditations meditations
   :state       {:time-selected     nil
                 :feelings-selected #{}}})
