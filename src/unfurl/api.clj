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
            [hickory.select  :as hs]
            [unfurl.common   :as co]))

(defn- http-get
  "'Friendly' form of http/get that adds request information to any exceptions that get thrown by clj-http."
  [{ url     :url
     options :options
     :as request }]
  (try
    (http/get url options)
    (catch clojure.lang.ExceptionInfo ei
      (throw (ex-info (.getMessage ei) { :request  request
                                         :response (ex-data ei)})))))

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

  Options are provided as key/value pairs, with any/all of the following keys:

    {
      :follow-redirects    (default: true)     - whether to follow 30x redirects
      :timeout-ms          (default: 1000)     - timeout in ms (used for both the socket and connect timeouts)
      :user-agent          (default: \"unfurl\") - user agent string to send in the HTTP request
      :max-content-length  (default: 16384)    - maximum length (in bytes) of content to retrieve (using HTTP range requests)
      :proxy-host          (default: nil)      - HTTP proxy hostname
      :proxy-port          (default: nil)      - HTTP proxy port
    }

  Thrown exceptions will usually be an ExceptionInfo with the ex-data containing:

    {
      :request  - the details of the HTTP request that was attempted
      :response - the details of the HTTP response that was received (comes directly from clj-http)
    }"
  [url & { :keys [ follow-redirects timeout-ms user-agent max-content-length proxy-host proxy-port ]
             :or { follow-redirects   true
                   timeout-ms         1000
                   user-agent         "unfurl"
                   max-content-length 16384
                   proxy-host         nil
                   proxy-port         nil }}]
  (if url
    ; Use oembed services first, and then fallback if it's not supported for the given URL
    (if-let [oembed-data (co/unfurl-oembed url)]
      oembed-data
      (let [request      { :url     url
                           :options (co/strip-nil-values { :accept           :html
                                                        :follow-redirects follow-redirects
                                                        :socket-timeout   timeout-ms
                                                        :conn-timeout     timeout-ms
                                                        :headers          {"Range"          (str "bytes=0-" (- max-content-length 1))
                                                                           "Accept-Charset" "utf-8, iso-8859-1;q=0.5, *;q=0.1"}
                                                        :client-params    {"http.protocol.allow-circular-redirects" false
                                                                           "http.useragent" user-agent}
                                                        :proxy-host       proxy-host
                                                        :proxy-port       proxy-port })}
            response     (http-get request)
            content-type (get (:headers response) "content-type")
            body         (:body response)]
        (if (.startsWith ^String content-type "text/html")
          (let [parsed-body (hc/as-hickory (hc/parse body))
                title-tags  (hs/select (hs/descendant (hs/tag :title)) parsed-body)
                meta-tags   (hs/select (hs/descendant (hs/tag :meta))  parsed-body)]
            (if meta-tags
              (merge (co/unfurl-html      title-tags meta-tags)
                     (co/unfurl-sailthru  meta-tags)
                     (co/unfurl-swiftype  meta-tags)
                     (co/unfurl-twitter   meta-tags)
                     (co/unfurl-opengraph meta-tags)))))))))
