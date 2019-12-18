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
      (is (= :body-needs @(subscribe [:view])))

      (dispatch [:change-view :types])
      (is (= :types @(subscribe [:view])))

      (dispatch [:change-view :guided-by])
      (is (= :guided-by @(subscribe [:view])))))

  (testing "user inputs work correctly"
    (run-test-sync
      (dispatch [:initialize-db])
      (dispatch [:update-time 20])
      (is (= 20 @(subscribe [:input :time])))

      (dispatch [:update-values :sit])
      (is (contains? @(subscribe [:input :body-needs]) :sit))

      (dispatch [:update-values :sit])
      (is (-> @(subscribe [:input :body-needs]) (contains? :sit) not)))))
