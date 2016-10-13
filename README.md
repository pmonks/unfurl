[![Build Status](https://travis-ci.org/pmonks/unfurl.svg?branch=master)](https://travis-ci.org/pmonks/unfurl)
[![Open Issues](https://img.shields.io/github/issues/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/issues)
[![License](https://img.shields.io/github/license/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/blob/master/LICENSE)
[![Dependencies Status](http://jarkeeper.com/pmonks/unfurl/status.svg)](http://jarkeeper.com/pmonks/unfurl)

# unfurl

This library implements "URL unfurling" approximately according to how Slack does it.
See [this blog post](https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs)
for more info.

## Installation

unfurl is available as a Maven artifact from [Clojars](https://clojars.org/org.clojars.pmonks/unfurl).
Plonk the following in your project.clj :dependencies, substitute "#.#.#" for the latest version number,
`lein deps` and you should be good to go:

```clojure
[org.clojars.pmonks/unfurl "#.#.#"]
```

The latest version is:

[![version](https://clojars.org/org.clojars.pmonks/unfurl/latest-version.svg)](https://clojars.org/org.clojars.pmonks/unfurl)

## Usage

The functionality is provided by the `unfurl.api` namespace.

Require it in the REPL:

```clojure
(require '[unfurl.api :as uf] :reload-all)
```

Require it in your application:

```clojure
(ns my-app.core
  (:require [unfurl.api :as uf]))
```

The library provides a single public method:

```
user=> (require '[unfurl.api :as uf] :reload-all)
nil
user=> (doc unfurl.api/unfurl)
-------------------------
unfurl.api/unfurl
([url & {:keys [follow-redirects timeout-ms user-agent max-content-length], :or {follow-redirects true, timeout-ms 1000, user-agent "unfurl", max-content-length 16383}}])
  Unfurls the given url, throwing an exception if the url is invalid, returning
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
      :user-agent         (default: "unfurl") - user agent string to send in the HTTP request
      :max-content-length (default: 16384)    - maximum length (in bytes) of content to retrieve (using HTTP range requests)
    }
nil
```

## Developer Information

[GitHub project](https://github.com/pmonks/unfurl)

[Bug Tracker](https://github.com/pmonks/unfurl/issues)

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
