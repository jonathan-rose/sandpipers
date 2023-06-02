(ns sandpipers.scenes.level-01
  (:require [quip.sprite :as qpsprite]
            [quip.utils :as qpu]
            [quip.tween :as qptween]))

(def light-green [133 255 199])

(defn animated-sandpiper
  [pos current-animation]
  (qpsprite/animated-sprite
   :player
   pos
   128
   128
   "sandpiper-spritesheet.png"
   :animations {:idle {:frames 1
                       :y-offset 0
                       :frame-delay 15}}
   :current-animation current-animation))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(animated-sandpiper [100 100] :idle)])

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background light-green)
  (qpsprite/draw-scene-sprites state))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens))

(defn tween-sandpiper
  [state rotation]
  (update-in state
             [:scenes :level-01 :sprites]
             (fn [sprites]
               [(qptween/add-tween
                 (first sprites)
                 (qptween/->tween :rotation rotation))])))

(defn handle-key-pressed
  [state e]
  (case (:key e)
    :left (tween-sandpiper state 90)
    :right (tween-sandpiper state -90) state))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01
   :key-pressed-fns [handle-key-pressed]})
