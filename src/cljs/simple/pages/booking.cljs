(ns simple.pages.booking
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-com.splits :as re-splits]
   [re-com.util :as re-util]
   [simple.subs :as subs]
   [simple.utils.mytable :as mytable]
   [simple.events :as events]))

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
     [:li>a  item1]
     [:li>a  item2]
     [:li>a  item3]])])

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


(defn row-button-demo
  []
  (let [col-widths {:sort "2.6em" :name "7.5em" :from "4em" :to "4em" :actions "4.5em"}
        rows       {"1" {:id "1" :sort 0 :name "Lee Junwoo" :from "18:00" :to "22:30"}
                    "2" {:id "2" :sort 1 :name "Thinh P. Tran" :from "18:00" :to "22:30"}
                    ;"2" {:id "2" :sort 1 :name "Time range 2 with some extra text appended to the end." :from "18:00" :to "22:30"}
                    "3" {:id "3" :sort 2 :name "Anh Viet" :from "06:00" :to "18:00"}}]
    [re-com/v-box
     :size "auto"
     :children [[mytable/data-table rows col-widths]]]))


(defn mydatepicker
  []
  [re-com/v-box
   :size "250px"
   :children [[re-com/datepicker
               :on-change #(js/alert "change")]]])


(defn greeting
  [message]
  (let [menu-on (re-frame/subscribe [::subs/menu-on])]
   [re-com/v-box
    :size "auto"
    :children [[:h1 (.toString @menu-on)]]]))


(defn title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :label (str "Welcome to " @name)
     :level :level1
     :style {:color "#ffffff"
             :background "#3477db"}]))

(defn test-button
  []
  (let [menu-on (re-frame/subscribe [::subs/menu-on])
        mydisplay (if @menu-on "none" "block")]
    [re-com/v-box
     :size "auto"
     :children [[re-com/button
                 :label "Menu"
                 :on-click #(js/alert "Clicked")
                 :style {:display mydisplay}]]]))

(defn simple-h-box
  []
  [re-com/h-box
   :width "100%"
   :height "100px"
   :gap "10px"
   :children [[my-left-sidebar]
              [mydatepicker]
              [row-button-demo]]])

(defn main-panel
  []
  [re-com/v-box
   :height "100%"
   :width "100%"
   :children [[title]
              [clock]
              [simple-h-box]]])




