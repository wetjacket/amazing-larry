(ns amazing-larry.routes.util)

(defn get-rand-elt
  "Return a random element of passed coll"
  [coll]
  (nth coll (-> (count coll) rand int)))

(defn img-reply
  "Construct an img reply, with optional header text."
  ([title img]
   (img-reply nil title img))
  ([text title img]
   {:text text
    :image_title title
    :image_url img}))
