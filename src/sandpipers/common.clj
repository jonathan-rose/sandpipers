(ns sandpipers.common
  (:require [quip.sprite :as qpsprite]
            [sandpipers.sprites.surf :as surf]))

;; beach colours
(def sand-yellow [244 201 103])
(def damp-sand-yellow [207 166 78])
(def wet-sand-yellow [169 130 52])
(def sea-blue [83 103 182])
(def sky-blue [98 179 250])

;; sandpiper colours
(def sandpiper-light [182 181 145])
(def sand-piper-mid [174 140 77])
(def sandpiper-dark [104 78 50])

;; misc colours
(def white [242 243 226])
(def black [60 58 42])


;;; line intersection functions
;;; we'll be using these for figuring out where the water is meeting
;;; the beach, so we can basically ignore cases where the lines are
;;; parallel (or even vertical).

(defn slope
  "Calculate the slope of a line described by two points.

  If the line is vertical just return infinity to avoid dividing by
  zero."
  [[x1 y1] [x2 y2]]
  (if (= x1 x2)
    ##Inf
    (/ (- y2 y1)
       (- x2 x1))))

(defn y-intercept
  "Determine where a line crosses the y axis given a slope and an example point.

  given       y = mx + b
  therefore   b = y - mx

  A vertical line will return either ##Inf or ##-Inf."
  [m [x y]]
  (- y (* m x)))

(def intersection-point
  "Find the intersection point of two non-parallel lines.

  given:
  y = m1x + b1
  y = m2x + b2

  therefore:
  m2x + b2 = m1x + b1
  m2x - m1x = b1 - b2
  (m2 -m1)x = b1 - b2
  x = (b1 -b2) / (m2 - m1)"
  (memoize
   (fn [p1 p2 p3 p4]
     (let [m1 (slope p1 p2)
           m2 (slope p3 p4)
           b1 (y-intercept m1 p1)
           b2 (y-intercept m2 p3)]
       (if (= m1 m2)
         [##Inf ##Inf]
         (let [x (/ (- b2 b1)
                    (- m1 m2))
               y (+ (* m1 x) b1)]
           [x y]))))))

;; Surf generation

(defn get-beach
  [{:keys [current-scene] :as state}]
  (->> (get-in state [:scenes current-scene :sprites])
       (filter (qpsprite/group-pred :beach))
       first))

(defn add-surf
  [{:keys [current-scene] :as state}]
  (let [beach (get-beach state)]
    (update-in state
               [:scenes current-scene :sprites]
               conj
               (surf/surf-particle (:intersection-point beach))
               (surf/surf-particle (:intersection-point beach)))))

;; @TODO: something funcky going on here, some sprites are ending up
;; as just {:tweens ()}, whatever, this clears them.
(defn remove-dead-sprites
  [{:keys [current-scene] :as state}]
  (update-in state [:scenes current-scene :sprites] (fn [ss]
                                                      (remove (fn [s]
                                                                (nil? (:sprite-group s)))
                                                              ss))))

;; Food generation


(defn get-food
  [{:keys [current-scene] :as state}]
  (->> (get-in state [:scenes current-scene :sprites])
       (filter (qpsprite/group-pred :food))))
