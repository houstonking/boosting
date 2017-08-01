(ns ensemble.entropy)

(defn dotp [xs ys] (reduce + (map * xs ys)))

(defn entropy-inner [p]
  (- (* p (/ (Math/log p) (Math/log 2)))))

(defn entropy
  ([dataset attr]
   (let [data (map attr dataset)
         freqs (frequencies data)
         total (reduce + (vals freqs))
         probs (map #(/ % total) (vals freqs))]
     (reduce + (map entropy-inner probs)))))

(defn information-gain
  ([dataset attr group-fn]
   (let [subsets (group-by group-fn dataset)
         n (count dataset)]
     (- (entropy dataset attr)
        (reduce + (for [[_ subset] subsets]
                    (* (/ (count subset) n) (entropy subset attr))))))))
