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

(defn- unfurl-oembed
  [url]
  ;####TODO: implement this - see http://oembed.com/ for details
  nil)

(defn- meta-tag-name
  [meta-tag]
  (if-let [meta-tag-name (:name meta-tag)]
    meta-tag-name
    (:property meta-tag)))

(defn- meta-tag-value
  [meta-tags tag-name]
  (first (map :content
              (filter #(= tag-name (meta-tag-name %))
                      (map :attrs meta-tags)))))

(defn- unfurl-html 
  [url title-tags meta-tags]
  (strip-nil-values {
                      :url         url
                      :title       (first (:content (first title-tags)))
                      :description (meta-tag-value meta-tags "description")
                    }))

(defn- unfurl-sailthru
  [url meta-tags]
  (strip-nil-values {
                      :url         url
                      :title       (meta-tag-value meta-tags "sailthru.title")
                      :description (meta-tag-value meta-tags "sailthru:description")
                      :preview-url (meta-tag-value meta-tags "sailthru.image.full")
                    }))

(defn- unfurl-swiftype
  [url meta-tags]
  (strip-nil-values {
                      :url         url
                      :title       (meta-tag-value meta-tags "st:title")
                      :preview-url (meta-tag-value meta-tags "st:image")
                    }))

(defn- unfurl-twitter
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "twitter:url")
                      :title       (meta-tag-value meta-tags "twitter:title")
                      :description (meta-tag-value meta-tags "twitter:description")
                      :preview-url (meta-tag-value meta-tags "twitter:image")
                    }))

(defn- unfurl-opengraph
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "og:url")
                      :title       (meta-tag-value meta-tags "og:title")
                      :description (meta-tag-value meta-tags "og:description")
                      :preview-url (meta-tag-value meta-tags "og:image")
                    }))

(defn unfurl
  "Unfurls the given url, throwing an exception if the url is invalid, returning
  nil if the given url is nil or not supported, or a map containing some or all
  of the following keys (none of which are mandatory):

    {
      :url           - The url of the resource, according to the server
      :title         - The title of the given url
      :description   - The description of the given url
      :preview-url   - The url of a preview image for the given url
    }

  Options are provided as a map, with any/all of the following keys:

    {
      :follow-redirects   (default: true)     - whether to follow 30x redirects
      :timeout-ms         (default: 1000)     - timeout in ms (used for both the socket and connect timeouts)
      :user-agent         (default: \"unfurl\") - user agent string to send in the HTTP request
      :max-content-length (default: 16384)    - maximum length (in bytes) of content to retrieve (using HTTP range requests)
    }"
  ; Fancy options handling from http://stackoverflow.com/a/8660833/369849
  [url & { :keys [ follow-redirects timeout-ms user-agent max-content-length ]
             :or { follow-redirects   true
                   timeout-ms         1000
                   user-agent         "unfurl"
                   max-content-length 16384 }}]
  (if url
    ; Use oembed services first, and then fallback if it's not supported for the given URL
    (if-let [oembed-data (unfurl-oembed url)]
      oembed-data
      (let [response     (http/get url {:accept           :html
                                        :follow-redirects follow-redirects
                                        :socket-timeout   timeout-ms
                                        :conn-timeout     timeout-ms
                                        :headers          {"Range"          (str "0-" (- max-content-length 1))}
                                        :client-params    {"http.useragent" user-agent}})
            content-type (get (:headers response) "content-type")
            body         (:body response)]
        (if (.startsWith ^String content-type "text/html")
          (let [parsed-body (hc/as-hickory (hc/parse body))
                title-tags  (hs/select (hs/descendant (hs/tag :title)) parsed-body)
                meta-tags   (hs/select (hs/descendant (hs/tag :meta))  parsed-body)]
            (if meta-tags
              (merge (unfurl-html      url title-tags meta-tags)
                     (unfurl-sailthru  url meta-tags)
                     (unfurl-swiftype  url meta-tags)
                     (unfurl-twitter   meta-tags)
                     (unfurl-opengraph meta-tags)))))))))
