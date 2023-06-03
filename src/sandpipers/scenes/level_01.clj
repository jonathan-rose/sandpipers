(ns sandpipers.scenes.level-01
  (:require [quil.core :as q]
            [quip.collision :as qpcollision]
            [quip.sprite :as qpsprite]
            [quip.tween :as qptween]
            [quip.utils :as qpu]
            [sandpipers.common :as common]
            [sandpipers.sprites.beach :as beach]
            [sandpipers.sprites.food :as food]
            [quip.delay :as qpdelay]))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(beach/beach)])

(defn colliders
  []
  [(qpcollision/collider
    :beach
    :food
    qpcollision/identity-collide-fn
    (constantly nil)
    :collision-detection-fn (fn [{[ix iy] :intersection-point
                                  [wx wy] :wet-sand-point
                                  :as beach}
                                 {[x y] :pos
                                  :as  food}]
                              (not (and (<= ix x wx)
                                        (<= wy y iy)))))])

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (q/no-stroke)
  (qpu/background common/sky-blue)
  (-> state
      (qpsprite/draw-scene-sprites-by-layers [:food :player :surf :beach])))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      common/add-surf
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens
      common/remove-dead-sprites
      qpdelay/update-delays
      qpcollision/update-collisions))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01
   :colliders (colliders)
   :delays [(food/food-delay)]})
