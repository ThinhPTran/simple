(ns simple.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [re-com.core :as re-com]
    [simple.subs :as subs]
    [simple.events :as events]
    [simple.pages.booking :as booking]
    [simple.pages.chatting :as chatting]))


(defn sidebar []
  [:div#sidebar-wrapper
   (let [menu-on (re-frame/subscribe [::subs/menu-on])
         value (if @menu-on false true)
         menu-name (if @menu-on "Menu" "M")
         item1 (if @menu-on "Chat room" "C")
         item2 (if @menu-on "Meeting room reservation" "M")
         item3 (if @menu-on "Others" "O")]
    [:ul.sidebar-nav
     [:li.sidebar-brand>a {:onClick #(re-frame/dispatch [::events/menu-on value])} menu-name]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "chatting"])} item1]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "booking"])} item2]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "others"])} item3]])])

(defn left-sidebar
  []
  (let [menu-on (re-frame/subscribe [::subs/menu-on])
        mydisplay (if @menu-on "none" "block")
        myclass (if @menu-on "" "toggled")]
    [:div#wrapper
      {:class myclass}
      [sidebar]]))

(defn my-left-sidebar
  []
  (let [menu-on (re-frame/subscribe [::subs/menu-on])
        mysize (if @menu-on "250px" "50px")]
   [re-com/v-box
    :size mysize
    :children [[left-sidebar]]]))


;; (defn title []
;;   (let [name (re-frame/subscribe [::subs/name])]
;;     [re-com/title
;;      :label (str "Welcome to " @name)
;;      :level :level1
;;      :style {:color "#ffffff"
;;              :background "#3477db"}]))

(defn title
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     {:style {:background "#3477db"}}
     [:h1
      {:style {:color "#ffffff"}}
      (str "Welcome to " @name)]]))


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
                           :font-size "50px"
                           :background "#3477db"}}]
        [:div style time-str]))))

(defn my-simple-page
  []
  (let [options (re-frame/subscribe [::subs/main-page])
        my-main-page (cond
                       (= @options "chatting") chatting/main-page
                       (= @options "booking") booking/main-page
                       :else chatting/main-page)]
    [re-com/h-box
      :width "100%"
      :height "100px"
      :gap "10px"
      :children [[my-left-sidebar]
                 [my-main-page]]]))


(defn main-panel
  []
  [re-com/v-box
   :height "100%"
   :width "100%"
   :children [[title]
              [clock]
              [my-simple-page]]])





