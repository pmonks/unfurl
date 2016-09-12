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

(ns unfurl.api
  (:require [clj-http.client :as http]
            [hickory.core    :as hc]
            [hickory.select  :as hs]))

(defn- strip-nil-values
  "Strips entries with nil values from map m."
  [m]
  (apply dissoc                                                                                            
       m
       (for [[k v] m :when (nil? v)] k)))

(defn- get-meta-tag-name
  [meta-tag]
  (if-let [meta-tag-name (:name meta-tag)]
    meta-tag-name
    (:property meta-tag)))

(defn- meta-tag-value
  [meta-tags tag-name]
  (first (map :content (filter #(= tag-name (get-meta-tag-name %)) (map :attrs meta-tags)))))

(defn- parse-oembed-data
  [url]
  ;####TODO: implement this - see http://oembed.com/ for details
  nil)

(defn- parse-opengraph
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "og:url")
                      :title       (meta-tag-value meta-tags "og:title")
                      :preview-url (meta-tag-value meta-tags "og:image")
                    }))

(defn- parse-twitter-card
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "twitter:url")
                      :title       (meta-tag-value meta-tags "twitter:title")
                      :description (meta-tag-value meta-tags "twitter:description")
                      :preview-url (meta-tag-value meta-tags "twitter:image")
                    }))

(defn- parse-html-tags
  [url meta-tags]
  (strip-nil-values {
                      :url         url
                      :description (meta-tag-value meta-tags "description")
                    }))

(defn unfurl
  "Unfurls the given url, throwing various exceptions if the url is invalid,
  returning nil if the given url isn't supported, or a map containing some or
  all of the following keys (all of which may not be provided, or may be nil):

  {
    :url           - The url of the resource, according to the server
    :title         - The title of the given url
    :description   - The description of the given url
    :preview-url   - The url of a preview image for the given url
  }"
  ; Fancy options handling from http://stackoverflow.com/a/8660833/369849
  [url & { :keys [follow-redirects timeout-ms user-agent]
             :or {follow-redirects true
                  timeout-ms       1000
                  user-agent       "unfurl"}}]
  (if url
    (if-let [result (parse-oembed-data url)]
      result
      (let [response     (http/get url {:accept           :html
                                        :follow-redirects follow-redirects
                                        :socket-timeout   timeout-ms
                                        :conn-timeout     timeout-ms
                                        :headers          {"Range"          "0-32767"}  ; Grab first 32K only
                                        :client-params    {"http.useragent" user-agent}})
            content-type (get (:headers response) "content-type")
            body         (:body response)]
        (if (.startsWith content-type "text/html")
          (let [meta-tags (hs/select (hs/descendant (hs/tag :meta))
                                     (hc/as-hickory (hc/parse body)))]
            (if meta-tags
              (merge (parse-html-tags    url meta-tags)
                     (parse-twitter-card meta-tags)
                     (parse-opengraph    meta-tags)))))))))
