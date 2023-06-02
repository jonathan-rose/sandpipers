(ns sandpipers.scenes.menu
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.scene :as qpscene]
            [quip.tween :as qptween]
            [quip.utils :as qpu]
            [sandpipers.common :as common]
            [sandpipers.sprites.beach :as beach]))

(defn on-click-play
  "Transition from this scene to `:level-01` with a 30 frame fade-out"
  [state e]
  (qpscene/transition state :level-01 :transition-length 30))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(beach/beach)
   (qpbutton/button-sprite "Play"
                           [(* 0.5 (q/width))
                            (* 0.5 (q/height))]
                           :color common/black
                           :content-color common/white
                           :on-click on-click-play)])

(defn draw-menu
  "Called each frame, draws the current scene to the screen"
  [state]
  (q/no-stroke)
  (qpu/background common/sky-blue)
  (-> state
      (qpsprite/draw-scene-sprites-by-layers [:surf :beach])))

(defn update-menu
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
   :draw-fn draw-menu
   :update-fn update-menu
   :mouse-pressed-fns [qpbutton/handle-buttons-pressed]
   :mouse-released-fns [qpbutton/handle-buttons-released]})
