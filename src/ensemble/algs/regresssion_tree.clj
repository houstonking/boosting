(ns ensemble.algs.regression-tree
  (:require [incanter.charts :as ich]
            [incanter.core :refer [view]]
            [incanter.stats :refer [sample-normal]]
            [clojure.core.matrix :refer [add]]))

(def n-samples 1000)

(def test-data
  (let [a 0
        b 10
        xs (range 0 10 (/ (- b a) n-samples))
        ys (add (map #(Math/sin %) xs) (sample-normal n-samples :sd 0.2))]
    (map vector xs ys)))

(defn learn-regression-tree [data-tuples & {:as opts}]

  )
