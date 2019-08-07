(ns simisola.views)


(defn card [img-path]
  [:img.pa2.dib.w-25
   {:src img-path}])

(defn main-panel []
  [:section.tc.pv3.ph7
   [card "/images/meditation.jpg"]
   [card "/images/ritual_2.jpg"]
   [card "/images/practice.jpg"]
   [card "/images/develop.jpg"]
   [card "/images/creation_2.jpg"]])


