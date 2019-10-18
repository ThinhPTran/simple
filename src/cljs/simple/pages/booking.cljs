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



(defn row-button-demo
  []
  (let [col-widths {:sort "2.6em" :name "7.5em" :from "4em" :to "4em" :actions "4.5em"}
        rows       {"1" {:id "1" :sort 0 :name "Lee Junwoo" :from "18:00" :to "22:30"}
                    "2" {:id "2" :sort 1 :name "Thinh P. Tran" :from "18:00" :to "22:30"}
                    ;"2" {:id "2" :sort 1 :name "Time range 2 with some extra text appended to the end." :from "18:00" :to "22:30"}
                    "3" {:id "3" :sort 2 :name "Anh Viet" :from "06:00" :to "18:00"}}]
    [re-com/v-box
     :width "auto"
     :children [[mytable/data-table rows col-widths]]]))

(defn mydatepicker
  []
  [re-com/v-box
   :width "250px"
   :children [[re-com/datepicker
               :on-change #(js/alert "change")]]])

(defn main-page
  []
  [re-com/h-box
   :width "auto"
   :height "100%"
   :gap "10px"
   :children [[mydatepicker]
              [row-button-demo]]])






