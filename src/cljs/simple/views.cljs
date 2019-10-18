(ns simple.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [re-com.core :as re-com]
    [simple.subs :as subs]
    [simple.events :as events]
    [simple.pages.booking :as booking]
    [simple.pages.chatting :as chatting]))


(defn left-sidebar []
 (let [menu-on (re-frame/subscribe [::subs/menu-on])
         value (if @menu-on false true)
         menu-name (if @menu-on "Menu" "M")
         item1 (if @menu-on "Chat room" "C")
         item2 (if @menu-on "Meeting room reservation" "M")
         item3 (if @menu-on "Others" "O")]
    [:ul
     {:style {:list-style-type "none"
              :font-size "20px"
              :text-indent "10px"
              :margin "0px"
              :padding "0px"
              :line-height "45px"
              :background "#3477db"}}
     [:li>a {:onClick #(re-frame/dispatch [::events/menu-on value])
             :style {:color "#ffffff"}} menu-name]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "chatting"])
             :style {:color "#ffffff"}} item1]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "booking"])
             :style {:color "#ffffff"}} item2]
     [:li>a {:onClick #(re-frame/dispatch [::events/main-page "others"])
             :style {:color "#ffffff"}} item3]]))


(defn my-left-sidebar
  []
  (let [menu-on (re-frame/subscribe [::subs/menu-on])
        mysize (if @menu-on "250px" "50px")]
   [re-com/v-box
    :width mysize
    :height "100%"
    :style {:background "#3477db"}
    :children [[left-sidebar]]]))


(defn title
  []
  (let [name (re-frame/subscribe [::subs/name])]
     [re-com/h-box
       :width "100%"
       :height "60px"
       :style {:background "#3477db"}
       :children [[re-com/box
                   :size "9"
                   :style {:color "#ffffff"
                           :font-size "50px"}
                   :child (str "Welcome to " @name)]
                  [re-com/box
                   :size "1"
                   :style {:color "#ffffff"
                           :font-size "25px"}
                   :child [:a {:style {:color "#ffffff"}} (str "Login")]]]]))


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
        [re-com/box
         :width "100%"
         :height "60px"
         :style (:style style)
         :child time-str]))))

(defn my-simple-page
  []
  (let [options (re-frame/subscribe [::subs/main-page])
        my-main-page (cond
                       (= @options "chatting") chatting/main-page
                       (= @options "booking") booking/main-page
                       :else chatting/main-page)]
    [re-com/h-box
      :width "100%"
      :height "598px" ;; Make it dynamic later
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





