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
; SPDX-License-Identifier: Apache-2.0
;

(ns unfurl.api-test
  (:require [clojure.string :as s]
            [clojure.test   :refer :all]
            [unfurl.api     :refer :all]))

(println "\n☔️ Running tests on Clojure" (clojure-version) "/ JVM" (System/getProperty "java.version"))

; Tune down logging noise from Apache HTTP client - it's rather noisy by default
(.setLevel (java.util.logging.Logger/getLogger "org.apache.http") java.util.logging.Level/SEVERE)

; Bake in appropriate Unfurl options for testing purposes
(def tunfurl #(unfurl % :timeout-ms 5000 :http-headers {"Accept-Language" "en-US,en;q=0.5"}))

(defn check-exception-status-code [expected-status-code]
  (fn [ex]
    (let [actual-status-code (:status (:response (ex-data ex)))]
      (= expected-status-code actual-status-code))))

(deftest unfurl-test
  (testing "Empty or syntactically invalid URLs"
    (is (= nil (tunfurl nil)))
    (is (thrown? java.net.MalformedURLException (tunfurl "")))
    (is (thrown? java.net.MalformedURLException (tunfurl "not a url")))
    (is (thrown? java.net.UnknownHostException  (tunfurl "http://www.abcdefghijklmnopqrstuvwxyz.com/")))
    (is (thrown? java.net.ConnectException      (tunfurl "https://localhost:8443/"))))
  (testing "URLs that don't point to a resource"
    (is (thrown? clojure.lang.ExceptionInfo     (tunfurl "https://google.com/foo"))))   ; Note: should also be confirming (check-exception-status-code 404) here
  (testing "Incorrect content types"
    (is (= nil (tunfurl "http://www.apache.org/licenses/LICENSE-2.0.txt")))
    (is (= nil (tunfurl "http://www.ucolick.org/~diemand/vl/images/L800kpc_z0_0_poster.png")))
    (is (= nil (tunfurl "http://samples.mplayerhq.hu/SWF/test.swf"))))
  (testing "Valid URLs"
    ; Simple HTML metatag-only site
    (is (= { :title "Clojure" }
           (tunfurl "http://clojure.org/")))
    ; Site with HTML metatags plus (partial) OpenGraph tags
    (let [result (tunfurl "http://www.facebook.com/")]   ; We do it this way because Facebook's meta tags change frequently enough that testing precise values is painful
      (is (= (:title result)       "Facebook - Log In or Sign Up"))
      (is (not (s/blank? (:description result))))
      (is (= (:url result)         "https://www.facebook.com/"))
      (is (= (:preview-url result) "https://www.facebook.com/images/fb_icon_325x325.png")))

    ; Everything and the kitchen sink tags (OpenGraph, Twitter, Swiftype and Sailthru!)
; Commented out as TechCrunch web server's aren't reliable enough to use for unit testing - sometimes they work, sometimes they time out, sometimes they return a corrupted ZLIB stream, ...
;    (is (= (tunfurl "https://techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/")
;           { :url         "http://social.techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/"
;             :title       "It’s a long, hard road from idea to IPO"
;             :description "It may not seem it, but coming up with an idea for your startup is probably the easiest part of launching your own company. As one industry insider told me, there are a million ways to screw up that idea through poor execution, and many, many lose their way. Yet a precious few fight through [&helli…"
;             :preview-url "https://techcrunch.com/wp-content/uploads/2016/09/img_2835-1.jpg?w=533"
;           }))
    )

  (testing "Valid URLs that resist unfurling"
    (is (thrown? clojure.lang.ExceptionInfo (tunfurl "https://www.linkedin.com/in/pmonks/"))))  ; Note: should also be confirming (check-exception-status-code 999) here
  (testing "Informative exceptions"
    (is (thrown? clojure.lang.ExceptionInfo (tunfurl "https://www.linkedin.com/in/pmonks/")))))   ; Note: should also be confirming ex-info contains :request and :response keys here
