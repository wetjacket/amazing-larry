(ns amazing-larry.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [amazing-larry.handler :refer :all]))

(deftest test-app
  (testing "healthcheck route"
    (let [response (app (mock/request :get "/healthcheck"))]
      (is (= 200 (:status response)))
      (is (= "IMOK" (:body response))))))
