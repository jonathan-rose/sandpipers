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

(defn handle-player-movement
  [state e]
  (let [currently-held? (:held-keys state)]
    (cond
      (currently-held? :left) (qpsprite/update-sprites-by-pred state (qpsprite/group-pred :player) (fn [s]
                                                                                                     (update s :vel (fn [[xvel yvel]]
                                                                                                                      [-5 yvel]))))
      (currently-held? :right) (qpsprite/update-sprites-by-pred state (qpsprite/group-pred :player) (fn [s]
                                                                                                      (update s :vel (fn [[xvel yvel]]
                                                                                                                       [+5 yvel]))))
      (currently-held? :down) (qpsprite/update-sprites-by-pred state (qpsprite/group-pred :player) (fn [s]
                                                                                                      (update s :vel (fn [[xvek yvel]]
                                                                                                                       [0 yvel]))))
      :else state)))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01
   :key-pressed-fns [handle-player-movement]})
