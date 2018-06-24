(ns amazing-larry.routes.irc-quote
  (:refer-clojure :exclude [test])
  (:require [amazing-larry.conf :refer [conf]]
            [amazing-larry.routes.util :as util]
            [clojure.java.io :as io]))

(defn- irc-quote
  "Pull lines from the old IRC index files Lewis used to generate from
  Birchbot's channel logs (I think only #dev).  This turned out to be
  super popular as it made it easy for people to build their own !bang
  triggers by changing their nick."
  [index-dir nick]
  (try
    (with-open [r (io/reader (format "%s/%s.idx" index-dir nick))]
      (let [quotes (into [] (line-seq r))]
        (util/get-rand-elt quotes)))
    (catch java.io.FileNotFoundException e)))

(defn test
  [msg]
  (if-let [[_ match] (re-matches #"(?i)^!(\w+)" (:text msg))]
    match))

(defn reply
  [match]
  (if-let [quote (irc-quote (:irc-index-dir conf) match)]
    (let [[_ line timestamp] (re-matches #"^(.+?)(\(.+\))$" quote)]
      {:text (format "_ %s _ %s :mantelpiece_clock:" line timestamp)})))

(def routes [[test reply]])
