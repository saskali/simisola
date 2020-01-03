(ns simisola.views
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [simisola.events :as events]
    [simisola.routes :as routes]
    [simisola.subs :as subs]))


(defn view->next-view [view]
  (get {routes/body-needs routes/practice-types
        routes/practice-types routes/facilitator
        routes/facilitator routes/practice}
       view))


(defn view->title [view]
  (get {routes/body-needs "Would you like to..."
        routes/practice-types "Choose the desired type of practice"
        routes/facilitator "Any preferred person?"}
       view))


(defn button-pill-grow [attributes content]
  ^{:key content}
  [:a.grow.no-underline.br-pill.ba.ph3.mh2.pv2.mb2.dib.mid-gray.pointer
   attributes
   content])


(defn button [view attributes]
  (let [input (subscribe [:input])]
    (button-pill-grow attributes
                      (cond
                        (empty? (get @input view)) "Skip"
                        (= :guided-by view) "And go!"
                        :else "Next"))))


(defn time-span-selection [time-spans]
  (into [:div.w-40.pa3.mr2]
        (map (fn [time-span]
               (button-pill-grow
                {:on-click #(dispatch [:update-time time-span])}
                (str "< " time-span)))
             time-spans)))


(defn time-view [time-spans]
    [:div
     [:h1.f-headline.tc
      "How many minutes would you like to practice?"]

     [:div.f1.flex.justify-center
      [time-span-selection time-spans]]])


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
  (fn [view _]
    ^{:key view}
    (if (= :practice (view->next-view view))
      :final-input
      :input)))


(defmethod selection-view :input
  [view practice-vals]
  [:div
   [:h1.f-headline.tc
    (view->title view)]

   [:div.f1.flex.justify-center
    [selection practice-vals]]

   [:div.f1.flex.justify-center.mv3
    (button view {:on-click #(dispatch [:change-view (view->next-view view)])})]])



(defmethod selection-view :final-input
  [view practice-vals]
  [:div
   [:h1.f-headline.tc
    (view->title view)]

   [:div.f1.flex.justify-center
    [selection practice-vals]]

   [:div.f1.flex.justify-center.mv3
    (button view {:on-click #(dispatch [:make-suggestion])})]])


(defn main-panel []
  (let [view (subscribe [:view])
        practice-vals (subscribe [:practice-vals])]
    (fn []
      (condp = @view
        routes/time-span [time-view (:time-spans @practice-vals)]
        routes/body-needs [selection-view @view (:body-needs @practice-vals)]
        routes/practice-types [selection-view @view (:types @practice-vals)]
        routes/facilitator [selection-view @view (:guided-by @practice-vals)]
        [:div "Oooops something went wrong"]))))
