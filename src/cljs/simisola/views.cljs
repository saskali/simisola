(ns simisola.views
  (:require
    [simisola.config :as config]
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
                        (or (zero? (get @input view))
                            (empty? (get @input view))) "Skip"
                        (= :guided-by view) "And go!"
                        :else "Next"))))


(defn time-span-selection [max-time-span]
  (let [time (subscribe [:time])]
    [:div.f1.flex.justify-center
     [:div.slider
      [:input {:type "range"
               :value @time
               :min 0
               :max max-time-span
               :on-change #(dispatch [:update-time (-> % .-target .-value)])}]
      [:output#rangevalue @time]]]))


(defn time-view [view max-time-span]
    [:div
     [:h1.f-headline.tc.mv5
      "How many minutes would you like to practice?"]

     [time-span-selection max-time-span]

     [:div.f1.flex.justify-center.mv4
      (button view {:on-click #(dispatch [:change-view routes/body-needs])})]])


(defn selection [values]
  (into [:div.flex.justify-center.w-40.pa3.mr2]
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


(defn suggestion-view []
  (let [suggestion (subscribe [:suggestion])]
    (fn []
      [:div
       [:h1.f-headline.tc (:title @suggestion)]])))


(defn main-panel []
  (let [view (subscribe [:view])
        practice-vals (subscribe [:practice-vals])]
    (fn []
      [:div.simisola
       {:style {:background-image
                (if config/debug?
                  "url('/images/IMG_7825.JPG')"
                  "url('/home/kayla/repos/projects/tools/simisola/resources/public/images/IMG_7825.JPG')")}}
       (condp = @view
         routes/time-span [time-view @view (:time-span @practice-vals)]
         routes/body-needs [selection-view @view (:body-needs @practice-vals)]
         routes/practice-types [selection-view @view (:types @practice-vals)]
         routes/facilitator [selection-view @view (:guided-by @practice-vals)]
         routes/practice [suggestion-view]
         [:h1.f-headline.tc "Oooops something went wrong"])])))
