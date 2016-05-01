(ns gopher.core
  (:require
    [gopher.util :as util]
    [gopher.evaluator :as evaluator]
    [gopher.config :as config]
    [clojure.core.async :as async :refer [>! <! go go-loop]])
  (:import
      (java.io.StringWriter)
      (java.lang.thread))
 (:gen-class))



(defn make-comm [id config]
  (let [f (util/kw->fn id)]
    (f config)))

(defn -main  [& args]
  (let [config (config/read-config)
        inst-comm (fn []
                    (println ":: building com:" (:comm config))
                    (make-comm (:comm config) config))]
    (println ":: starting with config:" config)

    (go-loop [[in out stop] (inst-comm)]
      (println ":: waiting for input")
      (if-let [form (<! in)]
        (let [input (:input form)]
          (println ":: form >> " input)
          (if (evaluator/can-handle? input (:prefix config))
            (do
              (let [res (evaluator/eval-expr (subs input 1))]
                (println ":: => " res)
                (>! out (assoc form :evaluator/result res))))
            (do
              (>! out (assoc form :evaluator/result {:status true
         :input form
         :form form
         :result ""
         :output "Not a command"}))))
         (recur [in out stop]))
        (do
          (println ":: WARNING! The comms went down, going to restart.")
          (stop)
          (<! (async/timeout 3000))
          (inst-comm))))

    (.join (Thread/currentThread))))
