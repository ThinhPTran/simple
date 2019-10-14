(ns simple.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-com.splits :as re-splits]
   [re-com.util :as re-util]
   [simple.subs :as subs]
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

(defn data-row
  [row first? last? col-widths mouse-over click-msg]
  (let [mouse-over-row? (identical? @mouse-over row)]
    [re-com/h-box
     :class    "rc-div-table-row"
     :attr     {:on-mouse-over (re-com/handler-fn (reset! mouse-over row))
                :on-mouse-out  (re-com/handler-fn (reset! mouse-over nil))}
     :children [[re-com/h-box
                 :width (:sort col-widths)
                 :gap "2px"
                 :align :center
                 :children [[re-com/row-button
                             :md-icon-name    "zmdi zmdi-arrow-back zmdi-hc-rotate-90"
                             :mouse-over-row? mouse-over-row?
                             :tooltip         "Move this line up"
                             :disabled?       (and first? mouse-over-row?)
                             :on-click        #(reset! click-msg (str "move row " (:id row) " up"))]
                            [re-com/row-button
                             :md-icon-name    "zmdi zmdi-arrow-forward zmdi-hc-rotate-90"
                             :mouse-over-row? mouse-over-row?
                             :tooltip         "Move this line down"
                             :disabled?       (and last? mouse-over-row?)
                             :on-click        #(reset! click-msg (str "move row " (:id row) " down"))]]]
                [re-com/label :label (:name row) :width (:name col-widths)]
                [re-com/label :label (:from row) :width (:from col-widths)]
                [re-com/label :label (:to   row) :width (:to   col-widths)]
                [re-com/h-box
                 :gap      "2px"
                 :width    (:actions col-widths)
                 :align    :center
                 :children [[re-com/row-button
                             :md-icon-name    "zmdi zmdi-copy"
                             :mouse-over-row? mouse-over-row?
                             :tooltip         "Copy this line"
                             :on-click        #(reset! click-msg (str "copy row " (:id row)))]
                            [re-com/row-button
                             :md-icon-name    "zmdi zmdi-edit"
                             :mouse-over-row? mouse-over-row?
                             :tooltip         "Edit this line"
                             :on-click        #(reset! click-msg (str "edit row " (:id row)))]
                            [re-com/row-button
                             :md-icon-name    "zmdi zmdi-delete"
                             :mouse-over-row? mouse-over-row?
                             :tooltip         "Delete this line"
                             :on-click        #(reset! click-msg (str "delete row " (:id row)))]]]]]))

(defn data-table
  [rows col-widths]
  (let [large-font (reagent/atom false)
        mouse-over (reagent/atom nil)
        click-msg  (reagent/atom "")]
    (fn []
      [re-com/v-box
       :align    :start
       :gap      "10px"
       :children [[re-com/checkbox
                   :label     "Large font-size (row-buttons inherit their font-size from their parent)"
                   :model     large-font
                   :on-change #(reset! large-font %)]
                  [re-com/v-box
                   :class    "rc-div-table"
                   :style    {:font-size (when @large-font "24px")}
                   :children [[re-com/h-box
                               :class    "rc-div-table-header"
                               :children [[re-com/label :label "Sort"    :width (:sort    col-widths)]
                                          [re-com/label :label "Name"    :width (:name    col-widths)]
                                          [re-com/label :label "From"    :width (:from    col-widths)]
                                          [re-com/label :label "To"      :width (:to      col-widths)]
                                          [re-com/label :label "Actions" :width (:actions col-widths)]]]
                              (for [[_ row first? last?] (re-util/enumerate (sort-by :sort (vals rows)))]
                                ^{:key (:id row)} [data-row row first? last? col-widths mouse-over click-msg])]]
                  [re-com/h-box
                   :gap "5px"
                   :width "300px"
                   :children [[:span "clicked: "]
                              [:span.bold (str @click-msg)]]]]])))

(defn row-button-demo
  []
  (let [col-widths {:sort "2.6em" :name "7.5em" :from "4em" :to "4em" :actions "4.5em"}
        rows       {"1" {:id "1" :sort 0 :name "Time range 1" :from "18:00" :to "22:30"}
                    "2" {:id "2" :sort 1 :name "Time range 2" :from "18:00" :to "22:30"}
                    ;"2" {:id "2" :sort 1 :name "Time range 2 with some extra text appended to the end." :from "18:00" :to "22:30"}
                    "3" {:id "3" :sort 2 :name "Time range 3" :from "06:00" :to "18:00"}}]
    [re-com/v-box
     :size "auto"
     :children [[data-table rows col-widths]]]))


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
             :background "#0000ff"}]))

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




