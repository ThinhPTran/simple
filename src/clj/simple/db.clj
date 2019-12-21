(ns simple.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as str]))


;; Read cpi data start
;; Create DB
(def db-conf {:classname "org.sqlite.JDBC"
              :subprotocol "sqlite"
              :subname "resources/database.db"})

(defn create-table
  "create db and table"
  [table_name table_scheme]
  (try (jdbc/db-do-commands db-conf
            (jdbc/create-table-ddl table_name table_scheme))
       (catch Exception e
         (println (.getMessage e)))))

(defn drop-table
  [table_name]
  (try (jdbc/db-do-commands db-conf
            (jdbc/drop-table-ddl table_name))
       (catch Exception e
         (println (.getMessage e)))))

(drop-table :gci_user_mas)

(create-table :gci_user_mas [[:id :integer "PRIMARY KEY" "AUTOINCREMENT"]
                             [:user_name :varchar "NOT NULL"]
                             [:email :varchar "NOT NULL"]
                             [:password :varchar "NOT NULL"]
                             [:created_at :timestamp
                              "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])

(drop-table :gcb_rmbk_mas)

(create-table :gcb_rmbk_mas [[:id :integer "PRIMARY KEY" "AUTOINCREMENT"]
                             [:name :varchar "NOT NULL"]
                             [:regis_dt :varchar "NOT NULL"]
                             [:from_t :varchar "NOT NULL"]
                             [:to_t :varchar "NOT NULL"]
                             [:action :varchar "NOT NULL"]
                             [:created_at :timestamp
                              "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])


(def get_user_list_query
  "select * from gci_user_mas where 1 = 1 order by id asc")

(def get_user_query
  "select * from gci_user_mas where user_name = ?")

(defn get_user_list_func
  []
  (try (jdbc/query db-conf [get_user_list_query])
       (catch Exception e
         (println (.getMessage e)))))

(defn get_user_inf
  [user_name]
  (let [query_rs (jdbc/query db-conf [get_user_query user_name])]
    (->> query_rs
        (first))))

;;(get_user_inf "trphthinh")

;;(and (= "thinh" "thinh") (= "123456" "123456"))

(defn delete_user_list
  []
  (try (jdbc/delete! db-conf :gci_user_mas ["1 = 1"])
       (catch Exception e
         (println (.getMessage e)))))

(get_user_list_func)
;(delete_user_list)

(def validate_uniquity_query
  "select * from gci_user_mas where user_name = ? or email = ?")

(defn validate_uniquity_func
  [user_name email]
  (let [query_rs (jdbc/query db-conf [validate_uniquity_query user_name email])
        n_rs (count query_rs)
        is_exist (if (> n_rs 0) true false)]
    is_exist))

;;(validate_uniquity_func "" "")

(defn insert_new_user
  [user_data]
  (when (not (validate_uniquity_func (:user_name user_data) (:email user_data)))
    (jdbc/insert! db-conf :gci_user_mas user_data)))

;; (insert_new_user {:user_name "thinhptran"
;;                   :email "trphthinh@gmail.com"
;;                   :password "123456"})



