(ns amazing-larry.routes.yoda
  (:refer-clojure :exclude [test])
  (:require [clojure.string :as str]))

;; Starting to play with !yoda.
;;
;; I think I need to reconceive things a bit... pulling out
;; formatting, thinking of "suppliers" and "permuters," perhaps.
;; !yoda is a permuter.  Suppliers should be returning a standardized
;; record type.  This also suggests abstrating the whole (admittedly
;; very thin) formatting layer (maybe a good use Protocols).

(defn test
  [msg]
  (if-let [[_ match] (re-matches #"(?i)^!yoda\s+(.*)" (:text msg))]
    match))

(defn reply
  [match]
  {:text
   (->> (str/split match #"\s+")
        reverse
        (str/join " "))})

(def routes [[test reply]])
