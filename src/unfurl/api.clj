;
; Copyright © 2016 Peter Monks
;
; This Source Code Form is subject to the terms of the Mozilla Public
; License, v. 2.0. If a copy of the MPL was not distributed with this
; file, You can obtain one at https://mozilla.org/MPL/2.0/.
;
; SPDX-License-Identifier: MPL-2.0
;

(ns unfurl.api
  (:require [clojure.string  :as s]
            [clj-http.client :as http]
            [hickory.core    :as hc]
            [hickory.select  :as hs]))

(defn- strip-nil-values
  "Strips entries with nil values from map m."
  [m]
  (apply dissoc
         m
         (for [[k v] m :when (nil? v)] k)))

; See http://oembed.com/
(defn- unfurl-oembed
  [_]
  ;####TODO: implement this - it's more complex than other schemes
  nil)

(defn- meta-tag-name
  [meta-tag]
  (if-let [meta-tag-name (:name meta-tag)]
    meta-tag-name
    (:property meta-tag)))

(defn- meta-tag-value
  [meta-tags tag-name]
  (let [value (first (map :content
                          (filter #(= tag-name (meta-tag-name %))
                                  (map :attrs meta-tags))))
        value (when value (s/trim value))]
    (when (pos? (count value))
      value)))

(defn- unfurl-html
  [title-tags meta-tags]
  (strip-nil-values {:title       (first (:content (first title-tags)))
                     :description (meta-tag-value meta-tags "description")}))

; See https://getstarted.sailthru.com/site/horizon-overview/horizon-meta-tags/
(defn- unfurl-sailthru
  [meta-tags]
  (strip-nil-values {:title       (meta-tag-value meta-tags "sailthru.title")
                     :description (meta-tag-value meta-tags "sailthru.description")
                     :preview-url (meta-tag-value meta-tags "sailthru.image.full")}))

; See https://swiftype.com/documentation/meta_tags
(defn- unfurl-swiftype
  [meta-tags]
  (strip-nil-values {:title       (meta-tag-value meta-tags "st:title")
                     :preview-url (meta-tag-value meta-tags "st:image")}))

; See https://dev.twitter.com/cards/markup
(defn- unfurl-twitter
  [meta-tags]
  (strip-nil-values {:url         (meta-tag-value meta-tags "twitter:url")
                     :title       (meta-tag-value meta-tags "twitter:title")
                     :description (meta-tag-value meta-tags "twitter:description")
                     :preview-url (meta-tag-value meta-tags "twitter:image")}))

; See http://ogp.me/
(defn- unfurl-opengraph
  [meta-tags]
  (strip-nil-values {:url         (meta-tag-value meta-tags "og:url")
                     :title       (meta-tag-value meta-tags "og:title")
                     :description (meta-tag-value meta-tags "og:description")
                     :preview-url (meta-tag-value meta-tags "og:image")}))

(defn- http-get
  "Version of `clj-http`'s `http/get` fn that adds request information to any
  exceptions that get thrown."
  [{url :url options :options :as request}]
  (try
    (http/get url options)
    (catch clojure.lang.ExceptionInfo ei
      (throw (ex-info (.getMessage ei) {:request  request
                                        :response (ex-data ei)})))))

(defn unfurl
  "Unfurls the given `url` , returning `nil` if `url` is `nil` or not supported,
  or a map containing some or all of the following keys (all of which are
  optional):

  * `:url` (`String`)
    The url of the resource, according to the server.
  * `:title` (`String`)
    The title of the given url.
  * `:description` (`String`)
    A brief textual description of the given url.
  * `:preview-url` (`String`)
    The url of a preview image for the given url.

  Options are:

  * `:follow-redirects` (`boolean`, default `true`):
    Whether to follow 30x redirects.
  * `:timeout-ms` (`long`, default `1000`)
    Timeout in ms (used for both the socket and connect timeouts).
  * `:user-agent` (`String`, default `\"https://github.com/pmonks/unfurl\"`)
    User agent string to send in the HTTP request. This should be either a
    browser identification string, an email address, or a URL, as some servers
    will reject requests with User Agent values that aren't in one of these
    domains.
  * `:max-content-length` (`long`, default `16384`)
    Maximum length (in bytes) of content to retrieve, using HTTP range requests
    (the entire content does not normally need to be retrieved in order to get
    the metadata `unfurl` uses).
  * `:proxy-host` (`String`, default `nil`)
    HTTP proxy hostname.
  * `:proxy-port` (`long`, default `nil`)
    HTTP proxy port.
  * `:http-headers` (a map with `String` keys and `String` values, default `nil`)
    A map of any other HTTP request headers you might want `unfurl` to include
    in the requests it makes.

  Throws on I/O errors, usually an ExceptionInfo with the `ex-data` containing:

  * `:request` (a map with `String` keys and `String` values)
    Contains the details of the HTTP request that was attempted.
  * `:response` (a map with `String` keys and `String` values)
    Contains the details of the HTTP response that was received (directly from
    `clj-http`)."
  [url & { :keys [follow-redirects timeout-ms user-agent max-content-length proxy-host proxy-port http-headers]
             :or {follow-redirects   true
                  timeout-ms         1000
                  user-agent         "https://github.com/pmonks/unfurl"
                  max-content-length 16384
                  proxy-host         nil
                  proxy-port         nil
                  http-headers       nil}}]
  (when url
    ; Use oembed services first, and then fallback if it's not supported for the given URL
    (if-let [oembed-data (unfurl-oembed url)]
      oembed-data
      (let [request      {:url     url
                          :options (strip-nil-values {:accept           :html
                                                      :follow-redirects follow-redirects
                                                      :socket-timeout   timeout-ms
                                                      :conn-timeout     timeout-ms
                                                      :headers          (merge {"Range"          (str "bytes=0-" (dec max-content-length))
                                                                                "Accept"         "text/html"
                                                                                "Accept-Charset" "utf-8, iso-8859-1;q=0.5, *;q=0.1"}
                                                                                http-headers)
                                                      :client-params    {"http.protocol.allow-circular-redirects" false
                                                                         "http.useragent"                         user-agent}
                                                      :proxy-host       proxy-host
                                                      :proxy-port       proxy-port})}
            response     (http-get request)
            content-type (get (:headers response) "content-type")
            body         (:body response)]
        (when (s/starts-with? content-type "text/html")
          (let [parsed-body (hc/as-hickory (hc/parse body))
                title-tags  (hs/select (hs/descendant (hs/tag :title)) parsed-body)
                meta-tags   (hs/select (hs/descendant (hs/tag :meta))  parsed-body)]
            (if meta-tags
              (merge (unfurl-html      title-tags meta-tags)
                     (unfurl-sailthru  meta-tags)
                     (unfurl-swiftype  meta-tags)
                     (unfurl-twitter   meta-tags)
                     (unfurl-opengraph meta-tags))
              (throw (ex-info "No meta tags provided in response body"
                              {:request  request
                               :response response})))))))))
