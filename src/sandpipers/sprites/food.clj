(ns sandpipers.sprites.food
  (:require [quip.delay :as qpdelay]
            [sandpipers.common :as common]
            [quip.sprite :as qpsprite]
            [quip.utils :as qpu]
            [quil.core :as q]
            [quip.tween :as qptween]))

(defn rand-pos
  [intersection-point options-vector]
  (let [factor (rand)]
    (map +
         intersection-point
         (map (partial * factor) options-vector))))

(defn food
  "Returns a food if there is space in the beach."
  [intersection-point options-vector]
  (-> (qpsprite/image-sprite
       :food
       (rand-pos intersection-point options-vector)
       64
       48
       "img/crab-64.png"
       :rotation 70)
      (qptween/add-tween (qptween/->tween
                          :pos
                          8
                          :step-count 50
                          :update-fn qptween/tween-y-fn
                          :yoyo? true
                          :yoyo-update-fn qptween/tween-y-yoyo-fn
                          :repeat-times ##Inf))))

(defn food-delay
  []
  (qpdelay/->delay
   180
   (fn [{:keys [current-scene] :as state}]
     (let [{:keys [intersection-point wet-sand-point] :as beach} (common/get-beach state)
           existing-food (common/get-food state)
           options-vector (map - wet-sand-point intersection-point)
           size (qpu/magnitude options-vector)]
       (-> (if (< 300 size)
             (update-in state [:scenes current-scene :sprites]
                        conj (food intersection-point options-vector))
             state)
           (qpdelay/add-delay (food-delay)))))))
