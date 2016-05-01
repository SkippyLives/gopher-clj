(ns gopher.core
  (:require
    [gopher.util :as util]
    [gopher.evaluator :as evaluator]
    [clojure.core.async :as async :refer [>! <! go go-loop]])
  (:import
      (java.io.StringWriter)
      (java.lang.thread))
 (:gen-class))


(def connection {:api-url "https://slack.com/api" :token ""})

(def config {
  :comm :gopher.comms.slack-rtm/start
  :token ""
  :api-token ""
  :prefix "!"})

(defn make-comm [id config]
  (let [f (util/kw->fn id)]
    (f config)))

(defn -main  [& args]
  (let [inst-comm (fn []
                    (println ":: building com:" (:comm config))
                    (make-comm (:comm config) config))]
    (println ":: starting with config:" config)

  (go-loop [[in out stop] (inst-comm)]
    (println ":: waiting for input")
    (if-let [form (<! in)]
      (let [input (:input form)
            res (evaluator/eval-expr input)]
        (println ":: form >> " input)
        (println ":: => " res)
        (>! out (assoc form :evaluator/result res))
        (recur [in out stop]))
      (do
        (println ":: WARNING! The comms went down, going to restart.")
        (stop)
        (<! (async/timeout 3000))
        (inst-comm))))

    (.join (Thread/currentThread))))
