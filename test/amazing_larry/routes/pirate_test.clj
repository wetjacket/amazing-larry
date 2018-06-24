(ns amazing-larry.routes.pirate-test
  (:require [clojure.test :refer :all]
            [amazing-larry.routes.pirate :as pirate]
            [clojure.string :as str]))

(deftest wrap-punctuation
  (is (= "foo!"
         ((pirate/wrap-punctuation (fn [_] str "foo")) "bar!"))))

(deftest wrap-capitalization
  (is (= "Foo"
         ((pirate/wrap-capitalization str/lower-case) "Foo"))))

(deftest to-pirate
  (testing "simple string, no replacement"
    (is (= "jello world"
           (pirate/to-pirate "jello world"))))

  (testing "simple string, with replacement"
    (is (= "ahoy helm"
           (pirate/to-pirate "hello admin"))))

  (testing "punctuated string"
    (is (= "ahoy helm!"
           (pirate/to-pirate "hello admin!"))))

  (testing "lots of punctuation"
    (is (= "ahoy helm!?.,:"
           (pirate/to-pirate "hello admin!?.,:"))))

  (testing "only punctuation"
    (is (= "!?.,:"
           (pirate/to-pirate "!?.,:"))))

  (testing "capitalization"
    (is (= "Ahoy world helm"
           (pirate/to-pirate "Hello world admin"))))

  (testing "camel capitalization"
    (is (= "Ahoy WoRlD helm"
           (pirate/to-pirate "HeLlO WoRlD admin")))))

(deftest suffix-flavor
  (testing "probability 0"
    (is (= "foo"
           (pirate/suffix-flavor "foo" 0))))

  (testing "probability 1"
    (is (> (count (pirate/suffix-flavor "foo" 1)) 3))))
