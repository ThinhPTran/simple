(ns simple.systemevents
  (:require [taoensso.sente :as sente]
            [ring.util.response :refer [response resource-response]]
            [clojure.data :as da :refer [diff]]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [taoensso.timbre :as timbre :refer (tracef debugf infof warnf errorf)]
            [clojure.string :as str]
            [simple.db :as db]))

;;(timbre/set-level! :trace) ; Uncomment for more logging
(reset! sente/debug-mode?_ true) ; Uncomment for extra debug info

(let [;; Serializtion format, must use same val for client + server:
      packer :edn ; Default packer, a good choice in most cases
      ;; (sente-transit/get-transit-packer) ; Needs Transit dep

      chsk-server
      (sente/make-channel-socket-server!
        (get-sch-adapter) {:packer packer})

      {:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      chsk-server]
  (def ring-ws-post ajax-post-fn)
  (def ring-ws-handoff ajax-get-or-ws-handshake-fn)
  (def receive-channel ch-recv)
  (def channel-send! send-fn)
  (def connected-uids connected-uids))


(defn connected-uids-change-handler [_ _ old new]
  (when (not= old new)
    (let [oldsk (:any old)
          newsk (:any new)
          newlogin (nth (diff oldsk newsk) 1)]
      (println (str "Connected uids change: " new))
      (println (str "oldsk: " oldsk))
      (println (str "newsk: " newsk))
      (println (str "newlogin: " newlogin)))))

;; We can watch this atom for changes if we like
(add-watch connected-uids :connected-uids connected-uids-change-handler)

;; Messages handler
(defn login-handler
  "Here's where you'll add your server-side login/auth procedure (Friend, etc.).
  In our simplified example we'll just always successfully authenticate the user
  with whatever user-id they provided in the auth request."
  [ring-req]
  (let [{:keys [session params]} ring-req
        {:keys [user-id password]} params
        dbinf (db/get_user_inf (str user-id))
        dbuser (:user_name dbinf)
        dbpass (:password dbinf)
        pass? (if (and (= user-id dbuser) (= password dbpass)) true false)]
    (println "Login request: " params)
    (println "Session: " (str session))
    (println "pass?: " (str pass?))
    (if pass?
      (do
        ;; Successful login!!!
        {:status 200 :session (assoc session :uid user-id)})
      (do
        ;; Fail
        {:status 400 :session (assoc session :uid user-id)}))))

(defn register-handler
  [data]
  (println "Registeering... ")
  (println "User: %s", (:user_name data))
  (println "email: ", (:email data))
  (println "password: ", (:password data))
  (db/insert_new_user data))


(defn- ws-msg-handler []
  (fn [{:keys [event] :as msg} _]
    (let [[id data :as ev] event]
      (case id
        :user/register (register-handler data)

        (println "Unmatched event: " id " data: " data)))))

(defn ws-message-router []
  (sente/start-chsk-router-loop! (ws-msg-handler) receive-channel))



