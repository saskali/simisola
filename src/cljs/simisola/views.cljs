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
                {:on-click #(dispatch [::events/update-time time-span])}
                (str "< " time-span)))
             time-spans)))


(defn feelings-selection [feelings]
  (into [:div.w-30.pa3.mr2]
        (map (fn [feeling]
               [:label.mh2.pointer
                {:on-change #(dispatch [::events/update-feeling feeling])}
                [:input.mr2.v-mid.pointer
                 {:type "checkbox"}]
                (name feeling)])
             feelings)))


(defn time-view [time-spans time-selected]
    [:div
     [:h1.f-headline.tc
      "How many minutes would you like to practice?"]

     [:div.f1.flex.justify-center
      [time-span-selection time-spans time-selected]]])


(defn feelings-view [feelings]
  [:div
   [:h1.f-headline.tc
    "How are you feeling?"]

   [:div.f1.flex.justify-center
    [feelings-selection feelings]]

   [:div.f1.flex.justify-center.mv3
    (button-pill-grow {:on-click #(dispatch [::events/change-view routes/practice])} "And go!")]])


(defn practice []
  [:div
   {:style {:max-width "80%"}}
   [:iframe.mv5
    {:allow-full-screen "allowfullscreen"
     :frame-border 0
     :align "right"
     :wmode "opaque"
     :height 480
     :width 854
     :src "https://www.youtube.com/embed/oJ_7Le2n7fU"}]])


(defn main-panel []
  (let [view (subscribe [::subs/view])
        time-spans (subscribe [::subs/time-spans])
        time-selected (subscribe [::subs/time-selected])
        feelings (subscribe [::subs/feelings])]

    (fn []
      (condp = @view
        routes/time-span [time-view @time-spans @time-selected]
        routes/feelings [feelings-view @feelings]
        routes/practice [practice]
        [:div "Oooops something went wrong"]))))
