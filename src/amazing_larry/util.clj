(ns amazing-larry.util
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

(defn pr-event
  "Print event to string, stripping out credentials"
  [event]
  (let [token (str/replace (:token event) #".+(.{4})" "***$1")]
    (pr-str (assoc event :token token))))

(defn init-timbre
  [log-file]
  (timbre/merge-config!
   {:timestamp-opts {:pattern "yyyy-MM-dd HH:mm:ss ZZ"}
    :appenders {:spit (appenders/spit-appender {:fname log-file})}
    :output-fn (partial timbre/default-output-fn {:stacktrace-fonts {}})}))
