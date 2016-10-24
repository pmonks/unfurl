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

(defproject org.clojars.pmonks/unfurl "0.2.0"
  :description      "'Unfurls' URLs approximately according to how Slack does it. See https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs for more info."
  :url              "https://github.com/pmonks/unfurl"
  :license          {:name "Apache License, Version 2.0"
                     :url  "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version "2.5.0"
  :dependencies     [
                      [org.clojure/clojure "1.8.0"]
                      [clj-http            "3.3.0" :exclusions [org.clojure/clojure]]
                      [org.jsoup/jsoup     "1.10.1"]
                      [hickory             "0.6.0" :exclusions [org.clojure/clojure org.jsoup/jsoup]]
                    ]
  :profiles         {:dev {:dependencies [[midje      "1.8.3"]]
                           :plugins      [[lein-midje "3.2.1"]]}   ; Don't remove this or travis-ci will assplode!
                     :uberjar {:aot :all}}
  :lein-release     {:deploy-via :clojars}
  )
