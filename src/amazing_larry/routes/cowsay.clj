(ns amazing-larry.routes.cowsay
  (:refer-clojure :exclude [test])
  (:require [clojure.java.shell :as shell]))

;; Requires the `cowsay' package

(defn test
  [msg]
  (if-let [[_ cowfile text] (re-matches
                             #"(?i)^!cowsay(?:/(\w+))?\s*(.{1,1024})s*"
                             (:text msg))]
    [cowfile text]))

(defn reply
  [[cowfile text]]
  (let [cmd (if cowfile
              ["cowsay" "-f" cowfile text]
              ["cowsay" text])]
    (try
      (let [status (apply shell/sh cmd)]
        (if (= 0 (:exit status))
          {:text (format "```%s```" (:out status))}
          {:text (format "%s :disappointed:" (:err status))}))
      (catch java.lang.Exception e))))

(def routes [[test reply]])
