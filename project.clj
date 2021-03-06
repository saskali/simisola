(defproject simisola "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.73"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]]

  :plugins []

  :jvm-opts ["-Xmx1g"]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs" "test"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]


  :aliases {"dev"  ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "test" ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "tests"]
            "prod" ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.4.1"]
                   [day8.re-frame/test "0.1.5"]
                   [day8.re-frame/tracing "0.5.3"]]}
   :prod
   {:dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]}})