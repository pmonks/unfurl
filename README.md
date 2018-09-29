[![Build Status](https://travis-ci.com/pmonks/unfurl.svg?branch=master)](https://travis-ci.com/pmonks/unfurl)
[![Open Issues](https://img.shields.io/github/issues/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/issues)
[![License](https://img.shields.io/github/license/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/pmonks/unfurl/status.svg)](https://versions.deps.co/pmonks/unfurl)

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

The library provides a single function - `unfurl`.  [The API documentation](https://pmonks.github.io/unfurl/) has full details, and [the unit tests](https://github.com/pmonks/unfurl/blob/master/test/unfurl/api_test.clj) provide some example usages.

## Tested Versions

unfurl is [tested on](https://travis-ci.com/pmonks/unfurl):

|                | JVM v1.6         | JVM v1.7       | JVM v1.8        | JVM v9         | JVM v10        | JVM v11         |
|           ---: |  :---:           |  :---:         |  :---:          |  :---:         |  :---:         |  :---:          |
| Clojure 1.5.1  | ❌<sup>1,2</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> |
| Clojure 1.6.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.7.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.8.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.9.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.10.0 | ❌<sup>2,3</sup> | ❌<sup>3</sup> | ✅             | ✅             | ✅             | ✅             |

<sup>1</sup> The version of `clj-http` used by this library only supports Clojure v1.6.0 and up

<sup>2</sup> Leiningen v2.8 only supports JVM v1.7 and up

<sup>3</sup> Clojure v1.10 only supports JVM v1.8 and up

## Developer Information

[GitHub project](https://github.com/pmonks/unfurl)

[Bug Tracker](https://github.com/pmonks/unfurl/issues)

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
