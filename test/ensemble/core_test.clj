(ns ensemble.core-test
  (:require [clojure.test :refer :all]
            [ensemble.validation :refer [k-fold-cross-val]]
            [ensemble.datasets :as ds]
            [ensemble.algs.id3 :as id3]))
