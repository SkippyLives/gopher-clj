(defproject gopher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.julienxx/clj-slack "0.5.3"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [stylefruits/gniazdo "0.4.1"]
                 [clj-http "2.0.0"]
                 [clojail "1.0.6"]
                 [cheshire "5.3.1"]]
  :main gopher.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
