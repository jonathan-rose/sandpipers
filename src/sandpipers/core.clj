(ns sandpipers.core
  (:gen-class)
  (:require [quip.core :as qp]
            [quip.sound :as qpsound]
            [sandpipers.scenes.menu :as menu]
            [sandpipers.scenes.level-01 :as level-01]))

(defn setup
  "The initial state of the game"
  []
  (qpsound/loop-music "Dewdrop Fantasy (shortened).wav")
  {})

(defn init-scenes
  "Map of scenes in the game"
  []
  {:menu     (menu/init)
   :level-01 (level-01/init)})

(defn get-screen-size []
  (.width (.getScreenSize (java.awt.Toolkit/getDefaultToolkit))))

(def sandpipers-game
  (qp/game {:title          "sandpipers"
            :size           [(min (- (get-screen-size) 200) 2400) 600]
            :setup          setup
            :init-scenes-fn init-scenes
            :current-scene  :menu}))

(defn -main
  [& args]
  (qp/run sandpipers-game))
