(ns ensemble.algs.util)

(defn tally [votes weights]
  (assert (= (count votes) (count weights)))
  (loop [results {}
         [v & vs] votes
         [w & ws] weights]
    (if (nil? v)
      results
      (recur (update results v (fnil + 0) w) vs ws))))

(defn weighted-voter
  "Given a map of {models --> weight} return a new function
   that will apply the fs to the input args and return
   the mode."
  [weighted-models]
  (let [models (keys weighted-models)
        weights (vals weighted-models)]
    (fn weighted-vote [& args]
      (let [votes (map apply models (repeat args))]
        (max-key val (tally votes weights))))))
