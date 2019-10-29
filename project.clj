(defproject simple "0.1.0-SNAPSHOT"
  :main simple.core
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/tools.reader]]
                 [thheller/shadow-cljs "2.8.62"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.3"]
                 [secretary "1.2.1"]
                 [re-frame "0.10.9"]
                 [re-com "2.6.0"]
                 [http-kit "2.2.0"]
                 [ring "1.6.0" :exclusions [commons-codec]]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.6.0"]
                 [com.taoensso/sente "1.11.0" :exclusions [org.clojure/core.async
                                                           org.clojure/tools.reader]]
                 [clj-time "0.15.2"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [org.clojure/data.csv "0.1.4"]
                 ;;; Transit deps optional; may be used to aid perf. of larger data payloads
                 ;;; (see reference example for details):
                 [com.cognitect/transit-clj  "0.8.319"]
                 [com.cognitect/transit-cljs "0.8.256"]]

  :plugins []

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]


  :aliases {"dev"  ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "prod" ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]]}

   :prod {}})

