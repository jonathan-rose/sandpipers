(ns sandpipers.scenes.level-01
  (:require [quip.sprite :as qpsprite]
            [quip.utils :as qpu]
            [quip.tween :as qptween]))

(def light-green [133 255 199])

(defn animated-sandpiper
  [pos current-animation]
  (assoc (qpsprite/animated-sprite
          :player
          pos
          128
          128
          "sandpiper-spritesheet.png"
          :animations {:idle      {:frames 1
                                   :y-offset 0
                                   :frame-delay 1}
                       :run-left  {:frames 3
                                   :y-offset 1
                                   :frame-delay 5}
                       :run-right {:frames 3
                                   :y-offset 2
                                   :frame-delay 5}
                       :eat       {:frames 1
                                   :y-offset 3
                                   :frame-delay 10}}
          :current-animation current-animation)
          :max-speed 12
          :min-speed -12))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(animated-sandpiper [400 100] :idle)])

(defn handle-player-movement
  [state]
  (let [currently-held? (:held-keys state)]
    (cond
      (currently-held? :left)
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :vel (fn [[xvel yvel]]
                            [(max (:min-speed s) (- xvel 0.5)) yvel]))
             ((fn [sprite]
                (if (not= (get sprite :current-animation) :run-left)
                  (qpsprite/set-animation sprite :run-left)
                  sprite))))))

      (currently-held? :right)
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :vel (fn [[xvel yvel]]
                            [(min (:max-speed s) (+ xvel 0.5)) yvel]))
             ((fn [sprite]
                (if (not= (get sprite :current-animation) :run-right)
                  (qpsprite/set-animation sprite :run-right)
                  sprite))))))

      (currently-held? :down)
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :vel (fn [[xvel yvel]] [0 yvel]))
             (qpsprite/set-animation :eat))))

      :else
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :vel (fn [[xvel yvel]]
                            (let [newx (* xvel 0.8)]
                              [(if (< (Math/abs newx) 0.1) 0 newx) yvel])))
             (qpsprite/set-animation :idle)))))))

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background light-green)
  (qpsprite/draw-scene-sprites state))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      handle-player-movement
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01})
