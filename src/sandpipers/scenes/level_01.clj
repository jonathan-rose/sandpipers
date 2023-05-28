(ns sandpipers.scenes.level-01
  (:require [quip.sprite :as qpsprite]
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
  (qpu/background common/sky-blue)
  (qpsprite/draw-scene-sprites state))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01})
