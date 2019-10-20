(ns simple.pages.chatting
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-com.splits :as re-splits]
   [re-com.util :as re-util]
   [simple.subs :as subs]
   [simple.utils.mytable :as mytable]
   [simple.events :as events]))


(defn title []
  [re-com/title
    :label (str "Welcome to chatting room")
    :level :level1
    :style {:color "#ffffff"
            :background "#3477db"}])


(defn main-page
  []
  [re-com/h-box
   :width "auto"
   :height "100%"
   :gap "10px"
   :children [[title]]])
