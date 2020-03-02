(ns commit-tree
  (:require [clojure.java.io :as io]
            [file-io :as fio]))

(def author_committer "Linus Torvalds <torvalds@transmeta.com> 1581997446 -0500")

(defn create-commit-tree
  "Once all of the error tests are passed, creates the commit and writes it"
  [args])


(defn file-path
  [file-name]
  (str ".git/objects/" (subs file-name 0 2) "/" (subs file-name 2)))

(defn parent-commit-handler
  "function takes care of the case where there is a p-switch"
  [parent-commits]
  (let [existence-eval (first (filter #(not (.exists (io/as-file (file-path %)))) parent-commits))
        ]))

(defn commit-tree
  "function for handling commit-tree"
  [[tree-addr m-switch message p-switch & parent-commits]]
  (cond
    (not (.isDirectory (io/file ".git"))) (println "Error: could not find database. (Did you run `idiot init`?)")
    (nil? tree-addr) (println "Error: you must specify a tree address.")
    (not (.exists (io/as-file (file-path tree-addr)))) (println "Error: no tree object exists at that address")
    (not= (fio/check-type (file-path tree-addr)) "tree") (println "Error: an object exists at that address, but it isn't a tree.")
    (not= m-switch "-m") (println "Error: you must specify a message.")
    (nil? message) (println "Error: you must specify a message with the -m switch.")
    (nil? p-switch) (print "hell")
    :else (parent-commit-handler parent-commits)))