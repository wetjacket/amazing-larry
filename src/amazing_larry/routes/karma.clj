(ns amazing-larry.routes.karma
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [slackbot-router.util :refer [text-match]]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname "db/karma.db"})

(def queries
  {:get-score-for-user
   "SELECT user, SUM(points) AS score
    FROM score
    WHERE user = ?"

   :get-notes-for-user
   "SELECT note, voter, created
    FROM score
    WHERE user = ? AND note NOT NULL AND points > 0
    LIMIT 3"

   :get-high-scores
   "SELECT user, SUM(points) AS score
    FROM score
    GROUP BY user
    ORDER BY score DESC
    LIMIT 10"})

(defn- get-score-for-user
  "Get vec of user and score."
  [user]
  (if-let [r (first (jdbc/query db [(:get-score-for-user queries) user]))]
    [(:user r) (:score r)]))

(defn- get-notes-for-user
  "Get coll of vecs of notes and voters, for user."
  [user]
  (jdbc/query db [(:get-notes-for-user queries) user]))

(defn get-user-status
  [user]
  (str
   (apply format "<@%s>: %s\n" (get-score-for-user user))
   (str/join "\n" (map (fn [r]
                         (format "_\"%s\"_ --<@%s> _ (%s) _"
                                 (:note r) (:voter r) (:created r)))
                       (get-notes-for-user user)))))

(defn get-high-scores
  "Get top-10 leaderboard str of user Karma."
  []
  (str/join "\n"
            (map (fn [r]
                   (format "<@%s>: %s" (:user r) (:score r)))
                 (jdbc/query db [(:get-high-scores queries)]))))

(defn test-adjust-user
  [msg]
  (if-let [[_ user op note] (re-matches #"^<@(\w+)>\s*(--|\+\+)(?:\s*(.+))?"
                                        (:text msg))]
    (let [voter (:user msg)
          points (if (= op "++") 1 -1)]
      [user points voter note])))

(defn reply-adust-user
  [[user points voter note :as coll]]
  (let [positive-karma (> points 0)]
    (if (and positive-karma (= user voter))
      {:text "Sorry, that's not how Karma works. :thinking_face:"}
      (do
        (jdbc/insert! db :score (zipmap [:user :points :voter :note] coll))
        (if positive-karma
          {:text ":purple_heart:"}
          {:text ":broken_heart:"})))))

(defn test-user-status
  [msg]
  (if-let [[_ user] (re-matches #"(?i)^!karma\s+<@(\w+)>\s*$" (:text msg))]
    user))

(defn reply-user-status
  [user]
  {:text (get-user-status user)})

(def routes
  [[test-adjust-user reply-adust-user]
   [test-user-status reply-user-status]
   (text-match #"(?i)^!karma\s*$" (get-high-scores))])
