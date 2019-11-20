(ns core-test
  (:require [clojure.test :refer [deftest testing is]]
            [day8.re-frame.test :refer [run-test-sync]]
            [re-frame.core :refer [dispatch subscribe]]
            [simisola.events]
            [simisola.subs]))

(deftest handlers-test
  (testing "routes work correctly"
    (run-test-sync
      (dispatch [:initialize-db])
      (is (= :time-span @(subscribe [:view])))

      (dispatch [:update-time])
      (is (= :feelings @(subscribe [:view])))))

  (testing "user inputs work correctly"
    (run-test-sync
      (dispatch [:initialize-db])
      (dispatch [:update-time 20])
      (is (= 20 @(subscribe [:state :time-input])))

      (dispatch [:update-feeling :tense])
      (is (contains? @(subscribe [:state :feelings-input]) :tense))

      (dispatch [:update-feeling :tense])
      (is (-> @(subscribe [:state :feelings-input]) (contains? :tense) not)))))
