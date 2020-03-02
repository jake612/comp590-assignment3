(ns cat-file
  (:require [clojure.java.io :as io]
            [file-io :as fio]
            [clojure.string :as str]))

(defn cat-file
  "function for handling cat-file command"
  [args]
  (let [address (second args)
        switch (first args)
        get-path #(str ".git/objects/" (subs % 0 2) "/" (subs % 2))]
    (cond
      (not (.isDirectory (io/file ".git"))) (println "Error: could not find database. (Did you run `idiot init`?)")
      (not= switch "-p") (println "Error: the -p switch is required")
      (nil? address) (println "Error: you must specify an address")
      (not (.exists (io/as-file (get-path address)))) (println "Error: that address doesn't exist")
      :else (print (second (str/split (fio/open-file (get-path address)) #"\000"))))))
