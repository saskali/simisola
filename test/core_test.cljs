(ns core-test
  (:require [clojure.test :refer [deftest testing is]]
            [day8.re-frame.test :refer [run-test-sync]]
            [re-frame.core :refer [dispatch subscribe]]
            [simisola.events]
            [simisola.subs]
            [simisola.routes :as routes]))

(deftest handlers-test
  (testing "routes work correctly"
    (run-test-sync
      (dispatch [:initialize-db])
      (is (= :time-span @(subscribe [:view])))

      (dispatch [:update-time])
      (is (= :feelings @(subscribe [:view])))

      (dispatch [:change-view routes/practice])
      (is (= :practice @(subscribe [:view])))))

  (testing "user inputs work correctly"
    (run-test-sync
      (dispatch [:initialize-db])
      (dispatch [:update-time 20])
      (is (= 20 @(subscribe [:selected :time])))

      (dispatch [:update-feeling :tense])
      (is (contains? @(subscribe [:selected :feelings]) :tense))

      (dispatch [:update-feeling :tense])
      (is (-> @(subscribe [:selected :feelings]) (contains? :tense) not)))))

