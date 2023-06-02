(ns sandpipers.sprites.surf
  (:require [quip.tween :as qptween]
            [quip.utils :as qpu]
            [quip.sprite :as qpsprite]
            [quil.core :as q]))

(defn update-rotation
  [{:keys [rvel] :as s}]
  (update s :rotation + rvel))

(defn update-yvel
  [s]
  (update s :vel (fn [[x y]]
                   [x (+ y 0.05)])))

(defn update-surf
  [s]
  (-> s
      qpsprite/update-pos
      update-rotation
      update-yvel))

(defn draw-surf
  [{:keys [pos alpha rotation]}]
  (q/fill 255 alpha)
  (let [[x y] pos
        [x1 y1] (map + pos
                     (qpu/rotate-vector [0 -5] rotation))
        [x2 y2] (map + pos
                     (qpu/rotate-vector [5 5] rotation))
        [x3 y3] (map + pos
                     (qpu/rotate-vector [-5 5] rotation))]
    (q/triangle x1 y1 x2 y2 x3 y3)))

(defn random-pos
  [[x y]]
  [(+ x (- (rand 20) 10)) (+ y (- (rand 20) 10))])

(defn random-vel
  []
  [(dec (rand 2)) (- (rand 1))])

(defn fade-tween
  []
  (qptween/->tween
   :alpha
   -255
   :step-count 30
   :on-complete-fn (constantly nil)))

(defn surf-particle
  [pos]
  (-> {:sprite-group :surf
       :pos (random-pos pos)
       :vel (random-vel)
       :rotation (rand 360)
       :rvel (rand 10)
       :alpha 255
       :update-fn update-surf
       :draw-fn draw-surf}
      (qptween/add-tween (fade-tween))))
