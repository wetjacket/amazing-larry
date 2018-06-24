(ns amazing-larry.routes.simple-reply
  (:require [slackbot-router.util :refer [text-match]]
            [amazing-larry.routes.simple-reply-coll :refer :all]
            [amazing-larry.routes.util :as util :refer [img-reply]]))

;; A mess of simple replies, acculated in the original Amazing-Larry, over
;; many, many years.

(def routes
  [(text-match #"(?i)^!snap"     "PUZZLE TIME.")
   (text-match #"(?i)^!bntj"     "Ben's Not That Jazzed.")
   (text-match #"(?i)^!womp"     ":trumpet::trumpet: http://wompwompwomp.com")
   (text-match #"(?i)^!fixed"    "It's Fixed In Replatform! :tada::rainbow:")
   (text-match #"(?i)^!hedge"    (img-reply ":evergreen_tree:" hedge-img))
   (text-match #"(?i)^!rejoice"  (img-reply "Rejoicing!" dancing-becky-img))
   (text-match #"(?i)^!wtf"      (img-reply wtf-emojis pancake-bunny-img))
   (text-match konami-code       birchbox-logo-ascii)

   (text-match #"(?i)^!milhouse"
               (img-reply "*MILHOUSE!*" (util/get-rand-elt milhausen)))

   (text-match #"(?i)^!(anxious|alg|anxious-logistics-guy|relax)"
               (img-reply ":cold_sweat:" anxious-img))

   (text-match #"(?i)^!(omg|amazing|amaze|amazaballs|amazeballs)"
               (img-reply ":unicorn_face:" amazing-img))

   (text-match #"(?i)^\s*buzz(kirk)?\s*" ascii-buzz)

   ;; At least I have chicken - https://youtu.be/mLyOj_QD4a4
   (text-match #"(?i)^.*deploy(ing)? (to )?prod(uction)?"
               "*leeeroy jeeenkins!!1* :hotdog:")

   ;; The BirchOx
   [(fn [m]
      (if (re-find #"(?i).*birchox.*" (:text m))
        m))
    (fn [m]
      (let [user (:user m)]
        (img-reply (format "<@%s>, why have you summoned the Birch Ox?? :lightning:" user)
                   "The Wise and Noble Birch Ox"
                   birch-ox-img)))]

   ;; !boromir
   [(fn [m]
      (if-let [[_ match] (re-matches #"(?i)!boromir\s+(.+)\s*" (:text m))]
        match))
    (fn [match]
      (img-reply (format "*One does not simply %s*" match)
                 "Boromir, High Warden of the White Tower"
                 boromir-img))]])
