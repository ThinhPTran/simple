(ns simple.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [simple.subs :as subs]))

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
              [greeting "Trinh"]]])

(defn main-panel []
  [re-com/v-box
   :height "100%"
   :width "100%"
   :children [[title]
              [simple-h-box]]])

