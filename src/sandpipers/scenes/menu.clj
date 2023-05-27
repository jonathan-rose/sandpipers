(ns sandpipers.scenes.menu
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.scene :as qpscene]
            [quip.utils :as qpu]))

(def white [230 230 230])
(def grey [57 57 58])
(def dark-green [41 115 115])

(defn on-click-play
  "Transition from this scene to `:level-01` with a 30 frame fade-out"
  [state e]
  (qpscene/transition state :level-01 :transition-length 30))

(defn sprites
  "The initial list of sprites for this scene"
  []
  [(qpbutton/button-sprite "Play"
                           [(* 0.5 (q/width))
                            (* 0.5 (q/height))]
                           :color grey
                           :content-color white
                           :on-click on-click-play)])

(defn draw-menu
  "Called each frame, draws the current scene to the screen"
  [state]
  (qpu/background dark-green)
  (qpsprite/draw-scene-sprites state))

(defn update-menu
  "Called each frame, update the sprites in the current scene"
  [state]
  (-> state
      qpsprite/update-scene-sprites))

(defn init
  "Initialise this scene"
  []
  {:sprites (sprites)
   :draw-fn draw-menu
   :update-fn update-menu
   :mouse-pressed-fns [qpbutton/handle-buttons-pressed]
   :mouse-released-fns [qpbutton/handle-buttons-released]})
