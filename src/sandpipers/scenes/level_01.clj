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
         :current-speed 0
         :max-speed 12
         :min-speed -12))

(defn sprites
  "The initial list of sprites for this scene"
  []
  (let [{:keys [sand-left sand-right] :as beach} (beach/beach)
        [x y] (map + sand-left (map - sand-right sand-left))]
    [(animated-sandpiper [x (- y 64)] :idle)
     beach]))

(defn update-vel
  [{:keys [current-speed] :as s} slope-vector]
  (assoc s :vel (map (partial * current-speed) slope-vector)))

(defn handle-player-movement
  [state]
  (let [currently-held? (:held-keys state)
        {:keys [sand-left sand-right] :as beach} (common/get-beach state)
        slope (beach/slope-vector sand-left sand-right)]
    (cond
      (currently-held? :left)
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :current-speed (fn [current-speed]
                                      (max (:min-speed s) (- current-speed 0.5))))
             (update-vel slope)
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
             (update :current-speed (fn [current-speed]
                                      (min (:max-speed s) (+ current-speed 0.5))))
             (update-vel slope)
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
             (assoc :current-speed 0)
             (assoc :vel [0 0])
             (qpsprite/set-animation :eat))))

      :else
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :player)
       (fn [s]
         (-> s
             (update :current-speed (fn [current-speed]
                                      (let [new-speed (* current-speed 0.8)]
                                        (if (< (Math/abs new-speed) 0.1) 0 new-speed))))
             (update-vel slope)
             (qpsprite/set-animation :idle)))))))

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
                              (not (<= ix x wx))))])

(defn draw-level-01
  "Called each frame, draws the current scene to the screen"
  [state]
  (q/no-stroke)
  (qpu/background common/sky-blue)

  ;; Draw score
  (qpu/fill common/white)
  (q/text-align :left :top)
  (q/text (str (:score state)) 30 30)

  (-> state
      (qpsprite/draw-scene-sprites-by-layers [:food :player :surf :beach])))

(defn update-score
  [{:keys [current-scene] :as state}]
  (let [crabs (common/get-food state)
        eaten (filter #(< 5 (:pecked-times %)) crabs)
        alive (remove #(< 5 (:pecked-times %)) crabs)
        increment (count eaten)]
    (-> state
        (update :score + increment)
        (update-in [:scenes current-scene :sprites]
                   (fn [sprites]
                     (filter
                      (fn [s]
                        (or (not= :food (:sprite-group s))
                            (< (:pecked-times s) 6)))
                      sprites))))))

(defn update-level-01
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      handle-player-movement
      update-score
      common/add-surf
      qpsprite/update-scene-sprites
      qptween/update-sprite-tweens
      common/remove-dead-sprites
      qpdelay/update-delays
      qpcollision/update-collisions))

(defn handle-peck
  [state e]
  (if (= :down (:key e))
    (let [{[bx by] :pos :as bird} (common/get-player state)]
      (qpsprite/update-sprites-by-pred
       state
       (qpsprite/group-pred :food)
       (fn [{[cx cy] :pos :as crab}]
         (if (qpcollision/w-h-rects-collide? bird crab)
           (-> crab
               (update :pos (fn [[x y]] [x (- y 5)]))
               (update :pecked-times inc))
           crab))))
    state))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-level-01
   :update-fn update-level-01
   :colliders (colliders)
   :delays [(food/food-delay)]
   :key-pressed-fns [handle-peck]})
