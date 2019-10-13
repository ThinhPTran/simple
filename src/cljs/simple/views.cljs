(ns simple.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [simple.subs :as subs]))

(defn clock
  []
  (let [time-color (re-frame/subscribe [::subs/time-color])
        timer (re-frame/subscribe [::subs/timer])]
    (fn clock-render
      []
      (let [time-str (-> @timer
                         .toTimeString
                         (clojure.string/split " ")
                         first)
            style {:style {:color @time-color
                           :font-size "50px"}}]
        [:div style time-str]))))

(defn anitem
  []
  [:div "tao lao"])

(defn greeting
  [message]
  [re-com/v-box
   :size "auto"
   :children [[:h1 message]]])

(defn title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :label (str "Hello from " @name)
     :level :level1]))

(defn simple-h-box
  []
  [re-com/h-box
   :width "100%"
   :height "100px"
   :gap "10px"
   :children [[greeting "Thinh"]
              [greeting "Bin"]
              [greeting "Trinh"]]])


(defn main-panel []
  [re-com/v-box
   :height "100%"
   :width "100%"
   :children [[title]
              [simple-h-box]
              [clock]]])


