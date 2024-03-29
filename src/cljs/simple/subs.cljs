(ns simple.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  ::timer
  (fn [db]
    (:timer db)))

(re-frame/reg-sub
  ::time-color
  (fn [db]
    (:time-color db)))

(re-frame/reg-sub
  ::menu-on
  (fn [db]
    (:menu-on db)))

(re-frame/reg-sub
  ::main-page
  (fn [db]
    (:main-page db)))

(re-frame/reg-sub
  ::id
  (fn [db]
    (:id db)))

(re-frame/reg-sub
  ::user_name
  (fn [db]
    (:user_name db)))

(re-frame/reg-sub
  ::email
  (fn [db]
    (:email db)))

(re-frame/reg-sub
  ::password
  (fn [db]
    (:password db)))
