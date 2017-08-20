(ns ensemble.entropy
  (:require [ensemble.datasets :refer [split-dataset-on]]))

(defn entropy-inner [p]
  (- (* p (/ (Math/log p) (Math/log 2)))))

(defn entropy
  "Dataset is [xs y]"
  ([dataset]
   (let [data  (map second dataset)
         freqs (frequencies data)
         total (reduce + (vals freqs))
         probs (map #(/ % total) (vals freqs))]
     (reduce + (map entropy-inner probs)))))

(defn information-gain
  "dataset is [xs y]
   attr is index of xs to calculate possible split"
  ([dataset attr]
   (let [subsets (split-dataset-on dataset attr)
         n (count dataset)]
     (- (entropy dataset)
        (reduce + (for [[_ subset] subsets]
                    (* (/ (count subset) n) (entropy subset))))))))
