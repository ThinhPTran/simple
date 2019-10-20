(defproject simple "0.1.0-SNAPSHOT"
  :main simple.core
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.62"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.3"]
                 [secretary "1.2.1"]
                 [re-frame "0.10.9"]
                 [re-com "2.6.0"]
                 [com.taoensso/sente "1.14.0"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [clj-time "0.12.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [com.taoensso/timbre       "4.10.0"]

                 ;;; TODO Choose (uncomment) a supported web server -----------------------
                 [http-kit                             "2.3.0"] ; Default
                 ;; [org.immutant/web                  "2.1.4"
                 ;;  :exclusions [ring/ring-core]]
                 ;; [nginx-clojure/nginx-clojure-embed "0.4.4"] ; Needs v0.4.2+
                 ;; [aleph                             "0.4.1"]
                 ;; -----------------------------------------------------------------------

                 [ring                      "1.7.1"]
                 [ring/ring-defaults        "0.3.2"] ; Includes `ring-anti-forgery`, etc.
                 ;; [ring-anti-forgery      "1.3.0"]

                 [compojure                 "1.6.1"] ; Or routing lib of your choice
                 [hiccup                    "1.0.5"] ; Optional, just for HTML

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

