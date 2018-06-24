(ns amazing-larry.routes
  (:require [slackbot-router.core :as router]
            [slackbot-router.util :refer [text-match]]
            [amazing-larry.routes.simple-reply :as simple-reply]
            [amazing-larry.routes.quote :as quote]
            [amazing-larry.routes.irc-quote :as irc-quote]
            [amazing-larry.routes.cowsay :as cowsay]
            [amazing-larry.routes.yoda :as yoda]
            [amazing-larry.routes.pickle :as pickle]
            [amazing-larry.routes.karma :as karma]
            [amazing-larry.routes.pirate :as pirate]))

(def message-table
  [(text-match "!ping" "!pong, the witch is dead")
   quote/routes
   cowsay/routes
   yoda/routes
   pickle/routes
   karma/routes
   irc-quote/routes
   pirate/routes
   simple-reply/routes])
