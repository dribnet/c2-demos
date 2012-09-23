(ns animating-bars.core
  (:use-macros [c2.util :only [bind!]])
  (:use [c2.core :only [unify]])
  (:require [c2.scale :as scale]
              [goog.dom :as dom]))

(def bardata-atom
  "bar data bound to atom. then updated to make a crude animating clock."
  (atom {"hours" 50, "minutes" 60, "seconds" 70, "millis" 80}))

(bind! "#bars" 
  (let [width 500 bar-height 20
        s (scale/linear :domain [0 100]
                        :range [0 width])]

    [:div.bars
     (unify @bardata-atom (fn [[label val]]
                   [:div {:style {:height bar-height
                                  :width (s val)
                                  :background-color "gray"}}
                      [:span {:style {:color "white"}} label]]))]))


(defn nextloop []
  (let [d (js/Date.)
        h {}
        h (assoc h "hours"   (/ (* (.getHours d) 100) 23))
        h (assoc h "minutes" (/ (* (.getMinutes d) 100) 59))
        h (assoc h "seconds" (/ (* (.getSeconds d) 100) 59))
        h (assoc h "millis"  (/ (* (.getMilliseconds d) 100) 999))]
  (reset! bardata-atom h)))

(defn animation-loop []
  (.requestAnimationFrame (dom/getWindow) animation-loop)
  (nextloop))

(animation-loop)