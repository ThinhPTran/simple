(ns simple.events
  (:require
   [re-frame.core :as re-frame]
   [simple.db :as db]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::timer
  (fn
    [db [_ value]]
    (db/set-timer db value)))

(re-frame/reg-event-db
  ::menu-on
  (fn
    [db [_ value]]
    (db/set-menu-on db value)))

(re-frame/reg-event-db
  ::main-page
  (fn
    [db [_ value]]
    (db/set-main-page db value)))

(re-frame/reg-event-db
  ::id
  (fn
    [db [_ value]]
    (db/set-id db value)))

(re-frame/reg-event-db
  ::user_name
  (fn
    [db [_ value]]
    (db/set-user-name db value)))

(re-frame/reg-event-db
  ::email
  (fn
    [db [_ value]]
    (db/set-email db value)))

(re-frame/reg-event-db
  ::password
  (fn
    [db [_ value]]
    (db/set-password db value)))
