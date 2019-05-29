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
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [hickory.core    :as hc]
            [hickory.select  :as hs]
            [unfurl.common   :as co]))

(defn unfurl
  "Unfurls the given url, returning a core.async channel in which a result
  will be made available. If the request is successful, the result will be a map
  containing some or all of the following keys (none of which are mandatory):

    {
      :url           - The url of the resource, according to the server
      :title         - The title of the given url
      :description   - The description of the given url
      :preview-url   - The url of a preview image for the given url
    }

  Options are provided as key/value pairs, with any/all of the following keys:

    {
      :follow-redirects    (default: true)     - whether to follow 30x redirects
      :timeout-ms          (default: 1000)     - timeout in ms (used for both
                                                 the socket and connect
                                                 timeouts)
      :user-agent          (default: \"unfurl\") - user agent string to send in
                                                   the HTTP request
      :max-content-length  (default: 16384)    - maximum length (in bytes) of
                                                 content to retrieve (using HTTP
                                                 range requests) * NOT WORKING *
      :proxy-host          (default: nil)      - HTTP proxy hostname
      :proxy-port          (default: nil)      - HTTP proxy port
    }

  If the request is not successful, the result will be nil if the resource is not
  HTML, or a map containing the following keys:

    {
      :status          - the HTTP status of the response - usually 0
      :success         - if the request was successfull - always false
      :body            - the body of the response - usually empty
      :headers         - the headers of the response - usually empty
      :trace-redirects - the redirects made by the request - unreliable
      :error-code      - the code of the error - usually :http-error
      :error-text      - the error message - usually [0]
    }
  
  Error messages are unfortunately not very informative at the moment. Make sure
  that the URL is valid, that it provides unfurling metadata, that its content
  type header is set to HTML, and that its CORS header is set to '*'.
  "
  [url & { :keys [ follow-redirects timeout-ms user-agent max-content-length proxy-host proxy-port ]
                   :or { follow-redirects   true
                        timeout-ms         1000
                        user-agent         "unfurl"
                        max-content-length 16384
                        proxy-host         nil
                        proxy-port         nil }}]
  (go
    (if url
    ; Use oembed services first, and then fallback if it's not supported for the given URL
      (if-let [oembed-data (co/unfurl-oembed url)]
        oembed-data
        (let [options (co/strip-nil-values {:with-credentials? false
                                            :accept           :html
                                            :follow-redirects follow-redirects
                                            :socket-timeout   timeout-ms
                                            :conn-timeout     timeout-ms
                                            :headers          {;; "Range"          (str "bytes=0-" (- max-content-length 1)) ;; * NOT WORKING *
                                                               "Accept-Charset" "utf-8, iso-8859-1;q=0.5, *;q=0.1"}
                                            :client-params    {"http.protocol.allow-circular-redirects" false
                                                               "http.useragent" user-agent}
                                            :proxy-host       proxy-host
                                            :proxy-port       proxy-port })
              response     (<! (http/get url options))
              content-type (get (:headers response) "content-type")
              body         (:body response)]
          (if (:success response)
            (if (.startsWith ^String content-type "text/html")
              (let [parsed-body (hc/as-hickory (hc/parse body))
                    title-tags  (hs/select (hs/descendant (hs/tag :title)) parsed-body)
                    meta-tags   (hs/select (hs/descendant (hs/tag :meta))  parsed-body)]
                (if meta-tags
                  (merge (co/unfurl-html      title-tags meta-tags)
                         (co/unfurl-sailthru  meta-tags)
                         (co/unfurl-swiftype  meta-tags)
                         (co/unfurl-twitter   meta-tags)
                         (co/unfurl-opengraph meta-tags)))))
            response))))))
