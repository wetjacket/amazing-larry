(ns amazing-larry.routes.pickle
  (:require [slackbot-router.util :refer [text-match]]
            [amazing-larry.conf :refer [conf]]
            [clojure.string :as str]
            [amazing-larry.slack :as slack]
            [clojure.java.shell :as shell]
            [clojure.math.combinatorics :as combo]))

(defn- rand-pickle-permutation []
  (let [pickles #{":pickle:" ":jarofpickles:" ":deadpickle:" ":picolas:"}
        rand-idx (-> (count pickles) rand int)]
    (apply str
           (nth (combo/permutations pickles) rand-idx))))

(defn- pickle-img
  "Pickle image found at URL."
  [url]
  (let [uuid (str (java.util.UUID/randomUUID))]
    (try
      (let [status (shell/sh (:pickle-img-cmd conf)
                             url
                             (:pickle-output-dir conf)
                             uuid)]
        (if (= 0 (:exit status))
          (let [filename (:out status)
                output-url (format "%s/%s" (:pickle-url-base conf) filename)]
            output-url)))
      (catch java.lang.Exception e))))

(defn test-pickle-user
  [msg]
  (if-let [[_ user] (re-matches #"(?i)^!pickle\s+<@(\w+)>" (:text msg))]
    user))

(defn reply-pickle-user
  [user]
  (if-let [response (slack/get-user user (:slack-bot-tok conf))]
    (let [avatar (get-in response [:profile :image_72])]
      (if-let [pickled-user-url (pickle-img avatar)]
        {:image_title (format "Freshly pickled <@%s>! :jarofpickles:" user)
         :image_url pickled-user-url}
        {:text (format "ENOPICKLING - could not pickle <@%s> :deadpickle:" user)}))
    {:text "ENOPICKLING - user not found"}))

;; We could likely support many more formats than these few.
;; https://www.imagemagick.org/script/formats.php
(defn test-pickle-url
  [msg]
  (if-let [[_ url] (re-matches #"(?i)^!pickle\s+<(https?://.+\.(?:jpg|png|gif|bmp))>"
                               (:text msg))]
    url))

(defn reply-pickle-url
  [img]
  (if-let [pickled-img-url (pickle-img img)]
    {:image_title  "Freshly pickled! :jarofpickles:"
     :image_url pickled-img-url}
    {:text (format "ENOPICKLING - could not URL %s :deadpickle:" img)}))

(def routes
  [(text-match #"(?i)^!pickles\s*$" {:text (rand-pickle-permutation)})

   [test-pickle-user reply-pickle-user]

   [test-pickle-url reply-pickle-url]

   (text-match #"(?i)^!pickle\s+.+"
               "Sorry, I don't know how to pickle that. :deadpickle:")])
