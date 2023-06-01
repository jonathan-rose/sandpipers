(ns sandpipers.sprites.beach
  "The beach; sand, sea and waves."
  (:require [quil.core :as q]
            [quip.tween :as qptween]
            [quip.utils :as qpu]
            [sandpipers.common :as common]))

(defn update-beach
  [s]
  s)

(defn draw-beach
  [{:keys [sea-level] :as s}]
  (let [bottom-left [0 (q/height)]
        bottom-right [(q/width) (q/height)]
        top-left [0 (* (q/height) 0.9)]
        top-right [(q/width) (* (q/height) 0.75)]]

    ;; Draw water
    (qpu/fill common/sea-blue)
    (q/rect 0 sea-level (q/width) (- (q/height) sea-level))

    ;; Draw the sand
    (qpu/fill common/sand-yellow)
    (q/begin-shape)
    (doall
     (map (partial apply q/vertex)
          [bottom-left
           top-left
           top-right
           bottom-right]))
    (q/end-shape)))

(defn wave-tween
  []
  (qptween/->tween
   :sea-level
   (* (q/height) 0.06)
   :step-count 300
   :easing-fn qptween/ease-in-out-quad
   :yoyo? true
   :repeat-times ##Inf
   ))

(defn beach
  []
  (qptween/add-tween
   {:sprite-group :beach
    :update-fn update-beach
    :draw-fn draw-beach
    :sea-level (* (q/height) 0.8)}
   (wave-tween)))
