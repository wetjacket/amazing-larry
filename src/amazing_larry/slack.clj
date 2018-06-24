(ns amazing-larry.slack
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [ring.util.response :refer [response status]]))

;; Slack API endpoints
(def slack-post-message-url "https://slack.com/api/chat.postMessage")
(def slack-user-info-url    "https://slack.com/api/users.info")

(defn- api-post
  "Generic API POST wrapper, returning Ring response map"
  [body url token]
  (let [authorization (format "Bearer %s" token)
        msg  {:headers {:authorization authorization}
              :content-type "application/json; charset=UTF-8"
              :body (json/write-str body)}]
    (http/post url msg)))

(defn- api-post-x-www-form-urlencoded
  "Generic API POST wrapper, but using Content-Type
  application/x-www-form-urlencoded for some older Slack API
  endpoints.  Expects map of form paramters.  Returns Ring response
  map"
  [form-params url token]
  (let [authorization (format "Bearer %s" token)
        msg  {:as :json
              :headers {:authorization authorization}
              :form-params form-params}]
    (http/post url msg)))

(defn- reformat-message
  "Perform last minute cleanup and formatting of our simplified message
  map, before passing to Slack, which has a more particular message
  layout requirement."
  [{:keys [text image_title image_url]}]
  (let [body {:text text}]
    (cond->  body
      (and image_title image_url)
      (assoc :attachments [{:title image_title :image_url image_url}]))))

(defn post-message
  "Post message to Slack channel, returning HTTP status."
  [body channel token]
  (-> (reformat-message body)
      (assoc :channel channel)
      (api-post slack-post-message-url token)
      :status))

;;
;; TODO - This should throw error if `ok' is not true
;;
;; https://api.slack.com/methods/users.info
;;
(defn get-user
  "Return info for Slack user."
  [user token]
  (let [response (api-post-x-www-form-urlencoded
                  {:user user} slack-user-info-url token)
        body (:body response)]
    (if (= (:ok body) true)
      (:user body))))

;;
;; Ring Middleware
;;
(defn wrap-slack-authn
  "Synchronous Ring middleware to verify Slack token in requests.  We
  allow exclusion of certain paths (eg, a healthcheck) matching
  regexs."
  [handler token excluded-regex-list]
  (fn [req]
    (if (or (= token (get-in req [:body :token]))
            (some #(re-find % (:uri req)) excluded-regex-list))
      (handler req)
      (-> (response "Invalid Slack Verification Token") (status 403)))))
