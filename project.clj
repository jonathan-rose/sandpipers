(defproject sandpipers "0.1.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [quip "2.0.3"]]
  :main ^:skip-aot sandpipers.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
