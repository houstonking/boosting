(ns ensemble.algs.id3
  (:require [ensemble.entropy :refer :all]
            [ensemble.datasets :refer [class-seq]]))

(defn node [x] x)

(defn id3 [dataset ->target-attribute]
  (let [classes (map ->target-attribute dataset)]
    (if (apply = classes)
      (node (first classes))
      (let [num-attrs (-> dataset first count dec)
            attribute-accessors (map ->nther (range num-attrs))
            [split-attr gain] (apply max-key
                                     val
                                     (zipmap (range num-attrs)
                                             (map (partial information-gain dataset)
                                                  attribute-accessors
                                                  attribute-accessors)))
            _ (println (format "splitting on %s with gain %s" split-attr gain))
            new-datasets (group-by (nth attribute-accessors split-attr) dataset)]
        {split-attr
         (into {} (for [[attr-val dataset] new-datasets]
                    [attr-val (id3 dataset)]))}
        ))))
