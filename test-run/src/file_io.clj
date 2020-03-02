(ns file-io
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.util.zip InflaterInputStream)
           (java.io ByteArrayOutputStream)))

(defn unzip
  "Unzip the given data with zlib. Pass an opened input stream as the arg. The
  caller should close the stream afterwards."
  [input-stream]
  (with-open [unzipper (InflaterInputStream. input-stream)
              out (ByteArrayOutputStream.)]
    (io/copy unzipper out)
    (->> (.toByteArray out)
         (map unchecked-byte)
         (map char)
         (apply str))))

(defn open-file
  "function which gets the file contents from a hashed object"
  [path]
  (let [open #(with-open [input (-> % io/file io/input-stream)]
                (unzip input))]
    (open path)))

(defn check-type
  "checks type of object"
  [file-path]
  (first (str/split (open-file file-path) #" ")))

