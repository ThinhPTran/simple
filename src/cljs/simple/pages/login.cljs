(ns simple.pages.login
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-com.splits :as re-splits]
   [re-com.util :as re-util]
   [simple.subs :as subs]
   [simple.utils.mytable :as mytable]
   [simple.events :as events]
   [simple.serverevents :as se]))


(defn name-form
  [name-atom]
  [re-com/input-text
   :model name-atom
   :on-change #(reset! name-atom %)])


(defn password-form
  [password]
  [re-com/input-password
   :model password
   :on-change #(reset! password %)])


(defn home-page []
  (let [myname (reagent/atom nil)
        mypass (reagent/atom nil)]
    (fn []
      [re-com/v-box
       :width "auto"
       :height "100%"
       :children [[:h2 "Welcome to Shinhan Center"]
                  [re-com/gap :size "10px"]
                  [re-com/label :label "Name"]
                  [name-form myname]
                  [re-com/gap :size "10px"]
                  [re-com/label :label "Password"]
                  [password-form mypass]
                  [re-com/gap :size "20px"]
                  [re-com/button
                   :label "Register"
                   :on-click #(se/login-request @myname @mypass)]]])))


(defn title []
  [re-com/title
    :label (str "Login")
    :level :level1
    :style {:color "#ffffff"
            :background "#3477db"}])


(defn main-page
  []
  [re-com/h-box
   :width "auto"
   :height "500px"
   :gap "10px"
   :children [[home-page]]])
