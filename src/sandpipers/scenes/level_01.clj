(ns sandpipers.scenes.level-01
  (:require [quip.sprite :as qpsprite]
            [quip.utils :as qpu]))

(def light-green [133 255 199])

(defn sprites
  "The initial list of sprites for this scene"
  []
  [])

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background light-green)
  (qpsprite/draw-scene-sprites state))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01})
