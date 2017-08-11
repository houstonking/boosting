(ns ensemble.datasets)

(defprotocol Dataset
  (class-seq [ds] "returns the seq of classes for the items in the dataset"))

(defn load-dataset [path attrs]
  (with-open [rdr (clojure.java.io/reader path)]
    (doall (for [line (line-seq rdr)]
             (zipmap attrs (clojure.string/split line #","))))))

(def car-dataset-attrs [:buying :maint :doors :persons :lug-boot :safety :class])
(def nursery-dataset-attrs [:parents :has-nurs :form :children
                            :housing :finance :social :health :class])

(def golf-dataset-attrs [:outlook :temperature :humidity :windy :play-golf])
(def golf-dataset
  (map (partial zipmap golf-dataset-attrs)
       [["rainy" "hot" "high" "false" "no"]
        ["rainy" "hot" "high" "true" "no"]
        ["overcast" "hot" "high" "false" "yes"]
        ["sunny" "mild" "high" "false" "yes"]
        ["sunny" "cool" "normal" "false" "yes"]
        ["sunny" "cool" "normal" "true" "no"]
        ["overcast" "cool" "normal" "true" "yes"]
        ["rainy" "mild" "high" "false" "no"]
        ["rainy" "cool" "normal" "false" "yes"]
        ["sunny" "mild" "normal" "false" "yes"]
        ["rainy" "mild" "normal" "true" "yes"]
        ["overcast" "mild" "high" "true" "yes"]
        ["overcast" "hot" "normal" "false" "yes"]
        ["sunny" "mild" "high" "true" "no"]]))
