(ns simple.systemevents
  (:require [taoensso.sente :as sente]
            [ring.util.response :refer [response resource-response]]
            [clojure.data :as da :refer [diff]]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [taoensso.timbre :as timbre :refer (tracef debugf infof warnf errorf)]
            [clojure.string :as str]))

;; (timbre/set-level! :trace) ; Uncomment for more logging
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
      (infof (str "Connected uids change: " new))
      (infof (str "oldsk: " oldsk))
      (infof (str "newsk: " newsk))
      (infof (str "newlogin: " newlogin)))))

;; We can watch this atom for changes if we like
(add-watch connected-uids :connected-uids connected-uids-change-handler)

(defn- ws-msg-handler []
  (fn [{:keys [event] :as msg} _]
    (let [[id data :as ev] event]
      (case id
        (println "Unmatched event: " id " data: " data)))))

(defn ws-message-router []
  (sente/start-chsk-router-loop! (ws-msg-handler) receive-channel))



