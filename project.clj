;
; Copyright © 2016 Peter Monks
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;

(defproject clj-commons/unfurl "0.12.0-SNAPSHOT"
  :description         "'Unfurls' URLs approximately according to how Slack does it. See https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs for more info."
  :url                 "https://github.com/clj-commons/unfurl"
  :license             {:spdx-license-identifier "Apache-2.0"
                        :name                    "Apache License, Version 2.0"
                        :url                     "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version    "2.8.1"
  :repositories        [["sonatype-snapshots" {:url "https://oss.sonatype.org/content/groups/public" :snapshots true}]
                        ["jitpack"            {:url "https://jitpack.io"}]]
  :dependencies        [[org.clojure/clojure "1.10.0"]
                        [org.clojure/clojurescript "1.10.520"]
                        [clj-http            "3.9.1" :exclusions [org.clojure/clojure]]
                        [cljs-http "0.1.46"]
                        [org.clojure/core.async "0.4.490"]
                        [org.jsoup/jsoup     "1.11.3"]
                        [hickory             "0.7.1" :exclusions [org.clojure/clojure org.jsoup/jsoup org.clojure/clojurescript viebel/codox-klipse-theme]]]
  :profiles            {:dev  {:plugins      [[lein-licenses "0.2.2"]
                                              [lein-codox    "0.10.4"]
                                              [lein-cljsbuild "1.1.7"]
                                              [lein-doo "0.1.10"]]
                               :dependencies [[doo "0.1.11"]]}
                        :1.5  {:dependencies [[org.clojure/clojure "1.5.1"]]}
                        :1.6  {:dependencies [[org.clojure/clojure "1.6.0"]]}
                        :1.7  {:dependencies [[org.clojure/clojure "1.7.0"]]}
                        :1.8  {:dependencies [[org.clojure/clojure "1.8.0"]]}
                        :1.9  {:dependencies [[org.clojure/clojure "1.9.0"]]}
                        :1.10 {:dependencies [[org.clojure/clojure "1.10.0"]]}}
  :deploy-repositories [["snapshots" {:url      "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password}]
                        ["releases"  {:url      "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password}]]
  :codox               {:source-uri "https://github.com/clj-commons/unfurl/blob/master/{filepath}#L{line}"}
  :cljsbuild           {:builds [{:id "test"
                                  :source-paths ["src" "test"]
                                  :compiler {:output-to "target/unit-tests.js"
                                             :optimizations :none
                                             :main unfurl.runner}}]
                        :test-commands {"unit-tests" ["lein" "doo" "firefox-headless" "test" "once"]}})

