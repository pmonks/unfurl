;
; Copyright © 2016-2017 Peter Monks
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

(ns unfurl.api-test
  (:require [midje.sweet :refer :all]
            [unfurl.api  :refer :all]))

; Tune down logging noise from Apache HTTP client - it's rather noisy by default
(.setLevel (java.util.logging.Logger/getLogger "org.apache.http.client") java.util.logging.Level/SEVERE)

; Bake in appropriate Unfurl options for testing purposes
(def tunfurl #(unfurl % :timeout-ms 5000))

(defn check-exception-status-code [expected-status-code]
  (fn [ex]
    (let [actual-status-code (:status (:response (ex-data ex)))]
      (= expected-status-code actual-status-code))))

(facts "Empty or syntactically invalid URLs"
  (tunfurl nil)
    => nil
  (tunfurl "")
    => (throws java.net.MalformedURLException)
  (tunfurl "not a url")
    => (throws java.net.MalformedURLException)
  (tunfurl "http://www.abcdefghijklmnopqrstuvwxyz.com/")
    => (throws java.net.UnknownHostException)
  (tunfurl "https://localhost:8443/")
    => (throws java.net.ConnectException)
)

(facts "URLs that don't point to a resource"
  (tunfurl "https://google.com/foo")
    => (throws clojure.lang.ExceptionInfo (check-exception-status-code 404))
)

(facts "Incorrect content types"
  (tunfurl "http://www.apache.org/licenses/LICENSE-2.0.txt")
    => nil
  (tunfurl "http://www.ucolick.org/~diemand/vl/images/L800kpc_z0_0_poster.png")
    => nil
  (tunfurl "http://samples.mplayerhq.hu/SWF/test.swf")
    => nil
)

(facts "Valid URLs"
  ; Simple HTML metatag-only site
  (tunfurl "http://clojure.org/")
    => { :title "Clojure"
       }

  ; Site with HTML metatags plus (partial) OpenGraph tags
  (tunfurl "http://www.facebook.com/")
    => { :url         "https://www.facebook.com/"
         :title       "Facebook - Log In or Sign Up"
         :description "Create an account or log into Facebook. Connect with friends, family and other people you know. Share photos and videos, send messages and get updates."
         :preview-url "https://www.facebook.com/images/fb_icon_325x325.png"
       }

  ; Everything and the kitchen sink tags (OpenGraph, Twitter, Swiftype and Sailthru!)
  (tunfurl "https://techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/")
    => { :url         "http://social.techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/"
         :title       "It’s a long, hard road from idea to IPO"
         :description "It may not seem it, but coming up with an idea for your startup is probably the easiest part of launching your own company. As one industry insider told me,.."
         :preview-url "https://tctechcrunch2011.files.wordpress.com/2016/09/img_2835-1.jpg"
       }
)

(facts "Valid URLs that resist unfurling"
  (tunfurl "https://www.linkedin.com/in/pmonks/")
    => (throws clojure.lang.ExceptionInfo (check-exception-status-code 999))
)

(facts "Informative exceptions"
  (tunfurl "https://www.linkedin.com/in/pmonks/")
    => (throws clojure.lang.ExceptionInfo (fn [ex] (not-any? nil? '((:request ex) (:response ex)))))
)

