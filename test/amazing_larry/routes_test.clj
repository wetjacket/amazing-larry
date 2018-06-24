(ns amazing-larry.routes-test
  (:require [clojure.test :refer :all]
            [slackbot-router.core :as router]
            [amazing-larry.routes :refer [message-table]]))

(deftest test-routes
  (testing "ping route"
    (let [request {:text "!ping"}
          response (router/route-message message-table request)]
      (is (= "!pong, the witch is dead"
             (:text response))))))
