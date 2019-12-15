(ns simple.serverevents
  (:require [taoensso.sente :as sente :refer (cb-success?)]
            [re-frame.core :as re-frame]
            [simple.events :as events]
            [clojure.string :as str]))


; Sente setup
(let [chsk-type :auto
      ;; Serializtion format, must use same val for client + server:
      packer :edn ; Default packer, a good choice in most cases
      ;; (sente-transit/get-transit-packer) ; Needs Transit dep

      {:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
        "/chsk" ; Must match server Ring routing URL
        {:type   chsk-type
         :packer packer})]
  (def receive-channel ch-recv)
  (def send-channel! send-fn)
  (def chsk chsk)
  (def chsk-state state))


; handle application-specific events
(defn- app-message-received [[msgType data]]
  (case msgType
    (.log js/console "Unmatched application event with msgType " (str msgType))))

; handle websocket handshake events
(defn- handshake-message-received [[wsid csrf-token hsdata isfirst]]
  (.log js/console "Handshake message:")
  (.log js/console "wsid: " (str wsid))
  (.log js/console "csrf-token: " (str csrf-token))
  (.log js/console "hsdata: " (str hsdata))
  (.log js/console "isFirst: " (str isfirst)))

(defn- event-handler [[id data] _]
  (case id
    :chsk/state (.log js/console "Channel state message received!!!")
    :chsk/recv (app-message-received data)
    :chsk/handshake (handshake-message-received data)
    (.log js/console "Unmatched connection event with " (str id) " and data " (str data))))

;; Message to server start
(defn register-request
  [user_name email password]
  (.log js/console "Login request!!!")
  (send-channel! [:user/register {:user_name  user_name
                                  :email email
                                  :password password}]))

(defn init-connection
  [user_name email password]
  (sente/ajax-lite "/login"
                         {:method :post
                          :headers {:X-CSRF-Token (:csrf-token @chsk-state)}
                          :params  {:user-id (str user_name)
                                    :password (str password)}}

                         (fn [ajax-resp]
                           (let [mystatus (:?status ajax-resp)
                                 login-successful? (if (= 200 mystatus) true false)]
                             (.log js/console "csrf-token: " (:csrf-token @chsk-state))
                             (.log js/console "Ajax login response: %s" (str ajax-resp))
                             (.log js/console "status: %s" (str mystatus))

                             (if-not login-successful?
                               (.log js/console "Login failed")
                               (do
                                 (.log js/console "Login successful")
                                 (sente/chsk-reconnect! chsk)
                                 (re-frame/dispatch [::events/id user_name])))))))

;; Message to server end

(sente/start-chsk-router-loop! event-handler receive-channel)
