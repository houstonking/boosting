(ns ensemble.validation
  (:require [ensemble.datasets :as ds]))

(defn partitions [m xs]
  (let [cnt (count xs)
        l   (quot cnt m)
        r   (rem cnt m)
        k   (* (inc l) r)]
    (concat
      (partition-all (inc l) (take k xs))
      (partition-all l (drop k xs)))))

(defn perform-fold [{:keys [train-data test-data learner classifier]}]
  (let [model (learner train-data)
        test-results
        (map vector
         (map second test-data)
         (map (partial classifier model)
              (map first test-data)))
        correct (reduce + (for [[k v] test-results] (if (= k v) 1 0)))]
    (double (/ correct
               (count test-results)))))

(defn k-fold-cross-val
  [k learner classifier dataset]
  (let [partitions (into #{} (partitions k (shuffle dataset)))
        fold-data (for [p partitions]
                    {:test-data p
                     :train-data (apply concat (disj partitions p))
                     :classifier classifier
                     :learner learner})
        folds (pmap perform-fold fold-data)]
    folds))
