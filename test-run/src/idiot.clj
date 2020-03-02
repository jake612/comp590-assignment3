(ns idiot
  (:require [clojure.java.io :as io]
            [hash-object]
            [cat-file]
            [init]
            [help]
            [write-wtree]
            [commit-tree]))

(defn -main
  "Main method for handling CLI"
  [& args]
  (let [num-args (count args)
        command (first args)
        check-first (fn [func] (if (or (= "-h" (second args)) (= "--help" (second args)))
                                 (help/help command)
                                 (func (rest args))))]
    (cond
      (or (= num-args 0) (= command "-h") (= command "--help")) (help/help "idiot")
      (= command "help") (help/help (second args))
      (= command "init") (check-first init/init)
      (= command "hash-object") (check-first hash-object/hash-object)
      (= command "cat-file") (check-first cat-file/cat-file)
      (= command "write-wtree") (check-first write-wtree/write-wtree)
      (= command "commit-tree") (check-first commit-tree/commit-tree)
      :else (println "Error: invalid command"))))
