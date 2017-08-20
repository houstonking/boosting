(ns ensemble.datasets
  (:require [incanter.core :as ic]
            [incanter.stats :as is]
            [incanter.charts :as charts]
            [incanter.datasets :as ids]))

(defn splitter [coll]
  [(vec (butlast coll)) (last coll)])

(defn load-dataset [path]
  (with-open [rdr (clojure.java.io/reader path)]
    (doall (for [line (line-seq rdr)]
             (splitter (clojure.string/split line #","))))))

(defn split-dataset-on [dataset attr]
  (group-by #(get (first %) attr) dataset))

(def golf-dataset
  [[["rainy" "hot" "high" "false"] "no"]
   [["rainy" "hot" "high" "true"] "no"]
   [["overcast" "hot" "high" "false"] "yes"]
   [["sunny" "mild" "high" "false"] "yes"]
   [["sunny" "cool" "normal" "false"] "yes"]
   [["sunny" "cool" "normal" "true"] "no"]
   [["overcast" "cool" "normal" "true"] "yes"]
   [["rainy" "mild" "high" "false"] "no"]
   [["rainy" "cool" "normal" "false"] "yes"]
   [["sunny" "mild" "normal" "false"] "yes"]
   [["rainy" "mild" "normal" "true"] "yes"]
   [["overcast" "mild" "high" "true"] "yes"]
   [["overcast" "hot" "normal" "false"] "yes"]
   [["sunny" "mild" "high" "true"] "no"]])
