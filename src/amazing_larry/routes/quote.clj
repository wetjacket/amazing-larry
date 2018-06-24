(ns amazing-larry.routes.quote
  (:refer-clojure :exclude [test])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "db/amazing-larry.db"})

(def queries
  {:verify-quote-set
   "SELECT 1
    FROM quote_set
    WHERE name = ? LIMIT 1"

   :get-quote-by-id
   "SELECT q.text AS text, q.quote_set_member_id AS id
    FROM quote q INNER JOIN quote_set qs on q.quote_set_id = qs.id
    WHERE qs.name = ? AND q.quote_set_member_id = ?"

   :get-random-quote
   "SELECT q.text AS text, q.quote_set_member_id AS id
    FROM quote q INNER JOIN quote_set qs on q.quote_set_id = qs.id
    WHERE qs.name = ?
    ORDER BY RANDOM() LIMIT 1"

   :get-quotes-for-pattern
   "SELECT q.text as text, q.quote_set_member_id AS id
    FROM quote q INNER JOIN quote_set qs on q.quote_set_id = qs.id
    WHERE qs.name = ? AND q.text LIKE ?"

   :get-quote-sets
   "SELECT qs.name, COUNT(*) AS count, qs.description
    FROM quote_set qs INNER JOIN quote q ON qs.id = q.quote_set_id
    GROUP BY qs.name
    ORDER BY qs.name ASC"})

(defn- verify-quote-set
  "Return true if passed quote-set exists.  We don't check if the
  quote-set has defined quotes."
  [quote-set]
  (if (= 1 (-> (jdbc/query db [(:verify-quote-set queries) quote-set])
               first :1))
    true))

(defn- get-quote-by-id
  "Return vec matrix of quote str and id for passed quote-set and id.
  Even though we're retruning a single quote, it's still in a matrix,
  for easier downstream formatting."
  [quote-set id]
  (let [id (Integer. id)]
    (vec (map (fn [r]
                [(:text r) (:id r)])
              (jdbc/query db [(:get-quote-by-id queries) quote-set id])))))

(defn- get-random-quote
  "Return vec matrix of random quote str and id, for passed quote-set.
  Even though we're retruning a single quote, it's still in a matrix,
  for easier downstream formatting."
  [quote-set]
  (vec (map (fn [r]
              [(:text r) (:id r)])
            (jdbc/query db [(:get-random-quote queries) quote-set]))))

(defn- get-matching-quotes
  "Return vec matrix of [quote id] for passed quote-set and SQL LIKE str."
  [quote-set pat]
  (vec (map (fn [r]
              [(:text r) (:id r)])
            (jdbc/query db [(:get-quotes-for-pattern queries) quote-set pat]))))

(defn- get-quote-sets
  "Return vec matrix of quote-sets and their descriptions."
  []
  (vec (map (fn [r]
              [(:name r) (:description r) (:count r)])
            (jdbc/query db [(:get-quote-sets queries)]))))

(defn- format-reply
  "Return reply map with a pretty string for arbitrary list of quote-ish
  things."
  [coll formatter]
  (hash-map
   :text
   (str/join "\n" (map #(apply format formatter %) coll))))

(defn test
  [msg]
  (if-let [[_ quote-set search-token]
           (re-matches #"(?i)^!(\w+)(?:\s+(.+)\s*)?$" (:text msg))]
    (try
      (if (or (= quote-set "quotes")
              (verify-quote-set quote-set))
        [quote-set search-token])
      (catch java.lang.Exception e))))

(defn reply
  [[quote-set search-token]]
  (cond
    (= quote-set "quotes")
    (-> (get-quote-sets)
        (format-reply "%s - _ %s [%s]_"))

    (nil? search-token)
    (-> (get-random-quote quote-set)
        (format-reply "_ %s _ (%d) :clapper:"))

    (and (not (nil? search-token))
         (re-find #"\d+" search-token))
    (-> (get-quote-by-id quote-set search-token)
        (format-reply "_ %s _ (%d) :clapper:"))

    :else
    (-> (get-matching-quotes quote-set search-token)
        (format-reply "_ %s _ (%d) :clapper:"))))

(def routes [[test reply]])
