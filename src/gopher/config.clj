 (ns gopher.config
   (:require [clojure.edn :as edn]))


 (defn read-config []
   (let [path "config.edn"]
     (edn/read-string (slurp path))))