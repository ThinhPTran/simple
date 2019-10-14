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
