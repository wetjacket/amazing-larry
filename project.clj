(defproject amazing-larry "0.0.1"
  :description "Something I can share with the rest of us"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [jcorrado/slackbot-router "0.1.1"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.0"]
                 [clj-http "3.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.async "0.3.465"]
                 [org.clojure/math.combinatorics "0.1.4"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.fzakaria/slf4j-timbre "0.3.8"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.xerial/sqlite-jdbc "3.20.1"]
                 [clj-time "0.14.2"]]
  :plugins [[lein-ring "0.12.2"]]
  :ring {:init amazing-larry.handler/init
         :handler amazing-larry.handler/app}
  :profiles  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring/ring-mock "0.3.0"]]}})
