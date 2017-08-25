(ns ensemble.core-test
  (:require [clojure.test :refer :all]
            [ensemble.validation :refer [k-fold-cross-val]]
            [ensemble.datasets :as ds]
            [ensemble.algs.id3 :as id3]
            [ensemble.algs.adaboost :as ab]))

(defn average [xs] (/ (reduce + xs) (count xs)))
