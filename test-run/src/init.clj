(ns init
  (:require [clojure.java.io :as io]))

(defn init
  "Function to initialize a new database"
  [args]
  (cond
    (> (count args) 0) (println "Error: init accepts no arguments")
    :else (if (.isDirectory (io/file ".git"))
            (println "Error: .git directory already exists")
            (do (io/make-parents ".git/objects/sample.txt")
                (println "Initialized empty Idiot repository in .git directory")))))