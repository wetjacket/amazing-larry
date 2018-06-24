(ns amazing-larry.conf
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.string :as str]))

;; Our EDN-formatted conf file
(def conf-resource-file "conf.edn")

(defn- parse-log-level
  [s]
  (-> s
      str/lower-case
      keyword
      #{:warn :info :debug}))

;; Environment variables that override conf file defaults
;; [ENV var, conf var name, optional parsing fn]
(def env-map [["AMAZING_LARRY_SLACK_VERIFICATION_TOKEN" :slack-vrfy-tok]
              ["AMAZING_LARRY_SLACK_BOT_TOKEN"          :slack-bot-tok]
              ["AMAZING_LARRY_LOG_FILE"                 :log-file]
              ["AMAZING_LARRY_LOG_LEVEL"                :log-level parse-log-level]
              ["AMAZING_LARRY_LOCAL_TZ"                 :local-tz]
              ["AMAZING_LARRY_HTTP_PUBLIC_DIR"          :http-public-dir]
              ["AMAZING_LARRY_IRC_INDEX_DIR"            :irc-index-dir]])

(defn- read-conf-file
  "Read EDN formatted conf resource file, returning conf map."
  [resource-file]
  (-> (io/resource resource-file)
      (slurp)
      (edn/read-string)))

(defn- read-env
  "Read environment variables, returning conf map."
  []
  (reduce (fn [m [env-var conf-var parser]]
            (let [val (System/getenv env-var)
                  parser (or parser identity)]
              (if val
                (assoc m conf-var (parser val))
                m)))
          (hash-map) env-map))

(defn- cleanup
  [conf]
  ;; EDN didn't like #"" regex, as it looks like a reader tag
  (update conf :whitelisted-resources (partial mapv re-pattern)))

(def conf (-> (merge (read-conf-file conf-resource-file)
                     (read-env))
              (cleanup)))
