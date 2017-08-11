(ns ensemble.algs.adaboost)

(defn build-final-classifier [weighted-classifiers])

(defn adaboost [dataset classifier & {:as opts :keys [iters classifier-opts]}]
  (loop [t iters
         distribution (repeat (count dataset) (/ 1 (count dataset)))
         weighted-classifiers {}]
    (if (zero? t)
      (build-final-classifier weighted-classifiers)
      (let [new-classifier (classifier )])

      )))
