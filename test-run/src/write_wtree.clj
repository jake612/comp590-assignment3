(ns write-wtree
  (:require [clojure.java.io :as io]
            [hash-handler :as hh]
            [hash-object :as ho]
            [clojure.string :as str]
            [git]))

(defn write-tree-object
  [tree]
  (let [header+tree (str "tree " (count tree) "\000" tree)
        address (hh/sha1-sum header+tree)
        path-of-destination-file (str ".git/objects/" (subs address 0 2) "/" (subs address 2))]
    (io/make-parents path-of-destination-file)
    (io/copy (ho/zip-str header+tree) (io/file path-of-destination-file))))

(defn hash->byte-array
  [hash]
  (let [splits (map (partial apply str) (partition-all 2 hash))
        bytes (for [colloc splits] (read-string (str "0x" colloc)))]
   (byte-array bytes)))

(defn generate-blob-entry
  "generates a blob entry for use in a tree"
  [file]
  (ho/write-blob (.getAbsolutePath file))
  (str "100644 " (.getName file) "\000" (new String (from-hex-string (hh/sha1-sum (hh/blob-data file))))))

(defn generate-tree-entry
  "generate tree entry"
  [entries]
  (let [entries-info (map (fn [string] (let [null-split (str/split string #"\000")]
                                               [(second null-split) string])) entries)
        alpha-order (sort-by second entries-info)
        tree-entry (apply str (map second alpha-order))]
    (write-object tree-entry)
    (str "04000 " (count tree-entry) "\000" tree-entry)))


(defn gen-tree
  "Function for recursively generating a tree given a directory"
  [dir]
  (let [files (file-seq dir)
        entries (for [file (rest files)] (if (.isDirectory file)
                                           (gen-tree file)
                                           (blob-entry-formatter file)))]
    (generate-tree-entry entries)))

(defn write-wtree
  "Function handles the write-wtree command"
  [args]
  (cond
    (> (count args) 0) (println "Error: write-wtree accepts no arguments")
    (not (.isDirectory (io/file ".git"))) (println "Error: could not find database. (Did you run `idiot init`?)")
    :else (->> (io/file "dir")
            gen-tree
            (git/address "tree")
            println)))

