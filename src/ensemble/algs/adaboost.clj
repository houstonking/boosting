(ns ensemble.algs.adaboost
  (:require [bigml.sampling.simple :refer [sample]]))

(defn ->weighted-dataset [dataset]
  (let [n (count dataset)]
    (zipmap dataset (repeat n (/ 1 n)))))

(defn sample-weighted-dataset [weighted-dataset]
  (take (count weighted-dataset)
        (sample (keys weighted-dataset)
                :weight weighted-dataset
                :replace true)))

(defn I [x] (if x 1 0 ))

(defn error-inner [classifier wds [xs y :as example]]
  (* (wds example) (I (not= y (classifier xs)))))

(defn error [classifier wds]
  (let [weight-total (reduce + (vals wds))
        res (/ (reduce +
                   (map (partial error-inner classifier wds)
                        (keys wds)))
               weight-total)]
    res))

(defn err->alpha [err k]
  (+ (Math/log (/ (- 1 err) err))
     (Math/log (- k 1))))

(defn classes [dataset]
  (into #{} (map second dataset)))

(defn reweight [wds classifier alpha]
  (let [un-norm (into {}
                      (for [[[xs y :as example] w] (seq wds)]
                        [example
                         (* w (Math/exp (* alpha (I (not= y (classifier xs))))))]))
        z (reduce + (vals un-norm))]
    (into {}
          (for [[[xs y :as example] w] (seq un-norm)]
            [example
             (/ w z)]))))

(defn adaboost
  "SAMME implementation of multiclass adaboost"
  [num-classifiers learner classify dataset]
  (let [weighted-dataset (->weighted-dataset dataset)
        k (count (classes dataset))]
    (loop [m num-classifiers
           wds weighted-dataset
           classifiers []]
      (if (zero? m)
        classifiers
        (let [model (learner (sample-weighted-dataset wds))
              classifier (partial classify model)
              err (error classifier wds)
              alpha (err->alpha err k)]
          (println alpha)
          (recur (dec m)
                 (reweight wds classifier alpha)
                 (conj classifiers [classifier alpha])))))))

(defn classify [weighted-classifiers xs]
  (first
   (apply max-key second
    (->> weighted-classifiers
         (map (fn [[classifier weight]]
                [(classifier xs) weight]))
         (group-by first)
         (map (fn [[class weights]]
                [class (reduce + (map second weights))]))))))
