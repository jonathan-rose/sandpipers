(ns sandpipers.scenes.level-01
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.tween :as qptween]
            [quip.utils :as qpu]
            [sandpipers.common :as common]
            [sandpipers.sprites.beach :as beach]))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(beach/beach)])

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (q/no-stroke)
  (qpu/background common/sky-blue)
  (-> state
      (qpsprite/draw-scene-sprites-by-layers [:surf :beach])))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      common/add-surf
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens
      common/remove-dead-sprites))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01})
