(ns simple.utils.mytable
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [re-com.util :as re-util]))


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
