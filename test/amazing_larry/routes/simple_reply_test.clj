(ns amazing-larry.routes.simple-reply-test
  (:require [clojure.test :refer :all]
            [slackbot-router.core :as router]
            [amazing-larry.routes.simple-reply :as simple-reply]
            [amazing-larry.routes :refer [message-table]]))

(deftest snap
  (testing "!snap"
    (let [request {:text "!snap"}
          response (router/route-message message-table request)]
      (is (= {:text "PUZZLE TIME."}
             response)))))

(def boromir-success {:text "*One does not simply test*",
                      :image_title "Boromir, High Warden of the White Tower",
                      :image_url "https://s3.amazonaws.com/com.birchbox.techops/img/boromir.jpg"})

(deftest boromir
  (testing "boromir reply route, via main table"
    (let [request {:text "!boromir test"}
          response (router/route-message message-table request)]
      (is (= boromir-success response)))))

(deftest boromir-via-ns-route-table
  (testing "boromir reply route, via amazing-larry.routes.simple-reply/routes"
    (let [request {:text "!boromir test"}
          response (router/route-message simple-reply/routes request)]
      (is (= boromir-success response)))))
