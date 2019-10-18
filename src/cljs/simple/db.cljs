(ns simple.db)

(def default-db
  {:name "Shinhan Center"
   :timer (js/Date.)
   :time-color "#ff1111"
   :menu-on true
   :main-page "chatting"})

(defn set-timer
  [db value]
  (assoc db :timer value))

(defn set-menu-on
  [db value]
  (assoc db :menu-on value))

(defn set-main-page
  [db value]
  (assoc db :main-page value))
