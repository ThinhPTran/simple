(ns simple.db)

(def default-db
  {:name "Shinhan Center"
   :timer (js/Date.)
   :time-color "#f88"
   :menu-on true
   :main-page "chatting"
   :id nil
   :user_name nil
   :email nil
   :password nil})

(defn set-timer
  [db value]
  (assoc db :timer value))

(defn set-menu-on
  [db value]
  (assoc db :menu-on value))

(defn set-main-page
  [db value]
  (assoc db :main-page value))

(defn set-id
  [db value]
  (assoc db :id value))

(defn set-user-name
  [db value]
  (assoc db :user_name value))

(defn set-email
  [db value]
  (assoc db :email value))

(defn set-password
  [db value]
  (assoc db :password value))
