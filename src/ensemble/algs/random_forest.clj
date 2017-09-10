(ns ensemble.algs.random-forest
  (:require [ensemble.algs.id3 :as id3]
            [ensemble.algs.util :refer [weighted-voter]]
            [bigml.sampling.simple :refer [sample]]))

(defn bagging [dataset learner & {:keys [num-samples num-]}]
  (let [datasets (take n (sample ))])
  )
