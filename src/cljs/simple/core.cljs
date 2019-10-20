(ns simple.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [simple.events :as events]
   [simple.views :as views]
   [simple.config :as config]
   [simple.serverevents :as se]))


(defonce time-updater (js/setInterval
                        #(re-frame/dispatch-sync [::events/timer (js/Date.)]) 1000))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))


(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/main-page "chatting"])
  (dev-setup)
  (mount-root))
;;  (se/init-connection))
