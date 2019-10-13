(ns simple.db)

(def default-db
  {:name "re-frame"
   :timer (js/Date.)
   :time-color "#f88"})

(defn set-timer
  [db value]
  (assoc db :timer value))
