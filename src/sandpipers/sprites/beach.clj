(ns sandpipers.sprites.beach
  "The beach; sand, sea and waves."
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.tween :as qptween]
            [quip.utils :as qpu]
            [sandpipers.common :as common]))

(defn update-beach
  [{:keys [sea-level sand-left sand-right] :as s}]
  (let [sea-left [0 sea-level]
        sea-right [(q/width) sea-level]]
    (assoc s :intersection-point (common/intersection-point
                                  sea-left
                                  sea-right
                                  sand-left
                                  sand-right))))

(defn draw-beach
  [{:keys [sea-level
           sand-left
           sand-right
           intersection-point] :as s}]
  (q/no-stroke)
  (let [bottom-left [0 (q/height)]
        bottom-right [(q/width) (q/height)]]

    ;; Draw water
    (qpu/fill common/sea-blue)
    (q/rect 0 sea-level (q/width) (- (q/height) sea-level))

    ;; Draw the sand
    (qpu/fill common/sand-yellow)
    (q/begin-shape)
    (doall
     (map (partial apply q/vertex)
          [bottom-left
           sand-left
           sand-right
           bottom-right]))
    (q/end-shape))

  ;; ;; Show sand and sea intersection lines
  ;; (q/stroke-weight 3)
  ;; (q/stroke [255 0 0])
  ;; (q/line [0 sea-level] [(q/width) sea-level])
  ;; (q/stroke [0 255 0])
  ;; (q/line sand-left sand-right)
  ;; (q/no-stroke)

  ;; ;; draw intersection-point
  ;; (let [[x y] intersection-point]
  ;;   (qpu/fill [255 0 0 100])
  ;;   (q/rect (- x 20) 0 40 (q/height))
  ;;   (q/ellipse x y 30 30))



  )

(declare pre-in-tween)
(declare in-tween)
(declare out-tween)
(declare bob-tween)

(defn max-out [] (* (q/height) 0.1))
(def preload 3)

(defn pre-in-tween
  []
  (qptween/->tween
   :sea-level
   preload
   :step-count 80
   :easing-fn qptween/ease-out-sine
   :on-complete-fn (fn [beach]
                     (qptween/add-tween beach (in-tween)))))

(defn in-tween
  []
  (qptween/->tween
   :sea-level
   (- 0 (max-out) preload)
   :step-count 400
   :easing-fn qptween/ease-in-out-cubic
   :on-complete-fn (fn [beach]
                     (qptween/add-tween beach (out-tween)))))

(defn out-tween
  []
  (qptween/->tween
   :sea-level
   (max-out)
   :step-count 300
   :easing-fn qptween/ease-in-out-quad
   :on-complete-fn (fn [beach]
                     (qptween/add-tween beach (bob-tween)))))

(defn bob-tween
  []
  (qptween/->tween
   :sea-level
   -3
   :easing-fn qptween/ease-in-out-sine
   :yoyo? true
   :repeat-times (inc (rand-int 2))
   :on-complete-fn (fn [beach]
                     (qptween/add-tween beach (pre-in-tween)))))

(defn beach
  []
  (-> {:sprite-group :beach
       :update-fn update-beach
       :draw-fn draw-beach
       :sea-level (* (q/height) 0.75)
       :sand-left [0 (* (q/height) 0.9)]
       :sand-right [(q/width) (* (q/height) 0.7)]
       ;; this will be set in the first update.
       :intersection-point [0 0]}
      (qptween/add-tween (out-tween))))
