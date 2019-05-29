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

(ns unfurl.common
  (:require [clojure.string  :as s]))

(defn strip-nil-values
  "Strips entries with nil values from map m."
  [m]
  (apply dissoc
         m
         (for [[k v] m :when (nil? v)] k)))

; See http://oembed.com/
(defn unfurl-oembed
  [url]
  ;####TODO: implement this
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

(defn unfurl-html
  [title-tags meta-tags]
  (strip-nil-values {
                      :title       (first (:content (first title-tags)))
                      :description (meta-tag-value meta-tags "description")
                    }))

; See https://getstarted.sailthru.com/site/horizon-overview/horizon-meta-tags/
(defn unfurl-sailthru
  [meta-tags]
  (strip-nil-values {
                      :title       (meta-tag-value meta-tags "sailthru.title")
                      :description (meta-tag-value meta-tags "sailthru.description")
                      :preview-url (meta-tag-value meta-tags "sailthru.image.full")
                    }))

; See https://swiftype.com/documentation/meta_tags
(defn unfurl-swiftype
  [meta-tags]
  (strip-nil-values {
                      :title       (meta-tag-value meta-tags "st:title")
                      :preview-url (meta-tag-value meta-tags "st:image")
                    }))

; See https://dev.twitter.com/cards/markup
(defn unfurl-twitter
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "twitter:url")
                      :title       (meta-tag-value meta-tags "twitter:title")
                      :description (meta-tag-value meta-tags "twitter:description")
                      :preview-url (meta-tag-value meta-tags "twitter:image")
                    }))

; See http://ogp.me/
(defn unfurl-opengraph
  [meta-tags]
  (strip-nil-values {
                      :url         (meta-tag-value meta-tags "og:url")
                      :title       (meta-tag-value meta-tags "og:title")
                      :description (meta-tag-value meta-tags "og:description")
                      :preview-url (meta-tag-value meta-tags "og:image")
                    }))
