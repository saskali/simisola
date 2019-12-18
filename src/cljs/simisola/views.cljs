(ns simisola.views
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [simisola.events :as events]
    [simisola.routes :as routes]
    [simisola.subs :as subs]))


(defn button-pill-grow [attributes content]
  [:a.grow.no-underline.br-pill.ba.ph3.mh2.pv2.mb2.dib.mid-gray.pointer
   attributes
   content])


(defn time-span-selection [time-spans]
  (into [:div.w-30.pa3.mr2]
        (map (fn [time-span]
               (button-pill-grow
                {:on-click #(dispatch [:update-time time-span])}
                (str "< " time-span)))
             time-spans)))


(defn time-view [time-spans time-selected]
    [:div
     [:h1.f-headline.tc
      "How many minutes would you like to practice?"]

     [:div.f1.flex.justify-center
      [time-span-selection time-spans time-selected]]])


(defn selection [values]
  (into [:div.w-30.pa3.mr2]
        (map (fn [value]
               ^{:key value}
               [:label.mh2.pointer
                {:on-change #(dispatch [:update-values value])}
                [:input.mr2.v-mid.pointer
                 {:type "checkbox"}]
                (name value)])
             values)))


(defmulti selection-view
  (fn [_ next-view _]
    (if (= :practice next-view)
      :final-input
      :input)))


(defmethod selection-view :input
  [practice-vals next-view title]
  [:div
   [:h1.f-headline.tc
    title]

   [:div.f1.flex.justify-center
    [selection practice-vals]]

   [:div.f1.flex.justify-center.mv3
    (button-pill-grow {:on-click #(dispatch [:change-view next-view])} "Next")]])


(defmethod selection-view :final-input
  [practice-vals _ title]
  [:div
   [:h1.f-headline.tc
    title]

   [:div.f1.flex.justify-center
    [selection practice-vals]]

   [:div.f1.flex.justify-center.mv3
    (button-pill-grow {:on-click #(dispatch [:make-suggestion])} "And go!")]])


(defn main-panel []
  (let [view (subscribe [:view])
        time-selected (subscribe [:input :time])
        practice-vals (subscribe [:practice-vals])]

    (fn []
      (condp = @view
        routes/time-span [time-view (:time-spans @practice-vals) @time-selected]
        routes/body-needs [selection-view (:body-needs @practice-vals)
                                          routes/practice-types
                                          "Would you like to..."]
        routes/practice-types [selection-view (:types @practice-vals)
                                              routes/facilitator
                                              "Choose the desired type of practice"]
        routes/facilitator [selection-view (:guided-by @practice-vals)
                                           routes/practice
                                           "Any preferred person?"]
        [:div "Oooops something went wrong"]))))
