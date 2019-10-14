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

