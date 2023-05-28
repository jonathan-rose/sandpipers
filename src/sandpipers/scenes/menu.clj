(ns sandpipers.scenes.menu
  (:require [quil.core :as q]
            [quip.sprite :as qpsprite]
            [quip.sprites.button :as qpbutton]
            [quip.scene :as qpscene]
            [quip.utils :as qpu]
            [sandpipers.common :as common]))

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
                           :color common/black
                           :content-color common/white
                           :on-click on-click-play)])

(defn draw-menu
  "Called each frame, draws the current scene to the screen"
  [state]
  (q/no-stroke)
  (qpu/background common/sky-blue)

  (let [bottom-left [0 (q/height)]
        bottom-right [(q/width) (q/height)]
        top-left [0 (* (q/height) 0.9)]
        top-right [(q/width) (* (q/height) 0.75)]
        wave-sea-level (* (q/height) 0.8)
        constant-sea-level (* (q/height) 0.86)]

    ;; Draw a wave
    (qpu/fill common/sea-blue)
    (q/rect 0 wave-sea-level (q/width) (- (q/height) wave-sea-level))


    ;; Draw the sea
    (qpu/fill common/deep-sea-blue)
    (q/rect 0 constant-sea-level (q/width) (- (q/height) constant-sea-level))

    ;; Draw the sand
    (qpu/fill common/sand-yellow)
    (q/begin-shape)
    (doall
     (map (partial apply q/vertex)
          [bottom-left
           top-left
           top-right
           bottom-right]))
    (q/end-shape))

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
