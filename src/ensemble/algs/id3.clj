(ns ensemble.algs.id3
  (:require [ensemble.entropy :refer :all]
            [ensemble.datasets :as ds :refer [split-dataset-on]]
            [bigml.sampling.simple :refer [sample]]
            [loom.io :refer [view]]
            [loom.label :as ll]
            [loom.graph :as lg]))

(defn node [dataset]
  (ffirst (max-key val (frequencies (map second dataset)))))

(defn id3
  "Takes as input tuples of the form [xs y]"
  [dataset & {:keys [max-depth use-random-subspaces?]
              :or {max-depth Long/MAX_VALUE
                   use-random-subspaces? false}}]
  (let [classes (map second dataset)]
    (if (or (= 0 max-depth)
            (apply = classes))
      (node dataset)
      (let [num-attrs (-> dataset ffirst count)
            attrs (range num-attrs)
            candidate-attrs (if-not use-random-subspaces?
                              attrs
                              (take (Math/ceil (Math/sqrt num-attrs)) (sample attrs)))
            [best-attr gain]
            (apply max-key
                   val
                   (zipmap candidate-attrs
                           (map (partial information-gain dataset)
                                candidate-attrs)))
            new-datasets (split-dataset-on dataset best-attr)]
        [best-attr
         (into {} (for [[attr-val dataset] new-datasets]
                    [attr-val (id3 dataset :max-depth (dec max-depth))]))]))))

(defn classify [decision-tree xs]
  (if-not (vector? decision-tree)
    decision-tree
    (let [[attr subtree] decision-tree]
      (recur (get subtree (get xs attr)) xs))))

(defn flatten-tree [decision-tree]
  (println decision-tree)
  (cond
    (vector? decision-tree)
    (let [[root splits] decision-tree
          grpd (group-by (comp vector? val) splits)
          base (into #{}
                     (map (fn [[split-val to]] [[root to] split-val]))
                     (get grpd false))
          _ (println base)
          all-edges (into base (for [[attr-val [new-root _]] (get grpd true)]
                                 [[root new-root] attr-val]))]
      (apply merge-with into {:edges all-edges} (map (comp flatten-tree second) (get grpd true))))
    :else
    {:nodes #{decision-tree}}))

(defn view-tree [decision-tree]
  (let [flattened-tree (flatten-tree decision-tree)]
    (view
     (cond-> (apply ll/add-labeled-edges (lg/digraph) (apply concat (:edges flattened-tree)))
       (:nodes flattened-tree)
       (#(apply lg/add-nodes % (:nodes flattened-tree)))))))
