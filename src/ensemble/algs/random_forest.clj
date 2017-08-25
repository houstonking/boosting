(ns ensemble.algs.random-forest
  (:require [ensemble.algs.id3 :as id3]
            [bigml.sampling.simple :refer [sample]]))

(defn nther* [i] (fn [x] (nth x i)))
(def nther (memoize nther*))

(defn ex-projector [idxs]
  (fn [xs] ((apply juxt (map nther idxs)) xs)))

(defn dataset-projector [idxs]
  (fn [[xs y]] [((ex-projector idxs) xs) y]))

(defn random-forest [dataset & {:keys [max-depth max-trees]
                                :or {max-depth Long/MAX_VALUE max-trees 100}}]
  (let [num-features (count (ffirst dataset))
        feature-indexes (range num-features)
        num-subfeatures (Math/ceil (Math/sqrt num-features))]
    (loop [n max-trees
           models []]
      (if (zero? n)
        models
        (let [features (take num-subfeatures (sample feature-indexes))]
          (recur (dec n)
                 (conj models
                       [(ex-projector features)
                        (id3/id3 (map (dataset-projector features) dataset)
                                 :max-depth max-depth)])))))))

(defn classify [random-forest xs]
  (first (apply max-key
                val
                (frequencies
                 (map (fn [[proj tree]]
                        (id3/classify tree (proj xs)))
                      random-forest)))))
