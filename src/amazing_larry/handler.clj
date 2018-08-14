(ns amazing-larry.handler
  (:require [slackbot-router.core :as router]
            [amazing-larry.util :as util]
            [amazing-larry.conf :refer [conf]]
            [amazing-larry.routes :refer [message-table]]
            [amazing-larry.slack :as slack :refer [wrap-slack-authn]]
            [ring.util.response :refer [response status]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.core.async :refer [chan go-loop put! <!]]
            [taoensso.timbre :as timbre :refer [warn info]]
            [clojure.string :as str]))

(def slack-events-c (chan 100))

(timbre/set-level! (:log-level conf))

(defn slack-event-handler
  [req]
  (let [event (:body req)]
    (info "Event received from Slack:" (util/pr-event event))
    (try
      (response (router/route-event event slack-events-c))
      (catch java.lang.Exception e
        (info "Unknown Slack message type:" (.getMessage e))
        (response "OK")))))

(defroutes app-routes
  (POST "/slack-event" [body] slack-event-handler)
  (GET "/healthcheck" [] (response "IMOK"))
  (route/not-found "Not Found"))

(defn init
  []
  ;; (info "DEBUG http-public-dir" (:http-public-dir conf))
  (util/init-timbre (:log-file conf))
  (go-loop []
    (let [msg (<! slack-events-c)
          slack-channel (:channel msg)]
      (if-let [reply (router/route-message message-table msg)]
        ;; TODO: Generalize exception handling in routing code pulling
        ;; it up to here, where we can easily log it.
        (let [status (slack/post-message reply slack-channel (:slack-bot-tok conf))]
          (warn (format "Posted to Slack channel: %s [%d]" slack-channel status)))))
    (recur)))

(def app
  (-> app-routes
      (wrap-json-response)
      (wrap-slack-authn (:slack-vrfy-tok conf) (:whitelisted-resources conf))
      (wrap-json-body {:keywords? true})
      ;; (wrap-file (:http-public-dir conf))
      (wrap-content-type)
      (wrap-not-modified)
      (wrap-defaults api-defaults)))
