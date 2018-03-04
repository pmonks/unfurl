[![Build Status](https://travis-ci.org/pmonks/unfurl.svg?branch=master)](https://travis-ci.org/pmonks/unfurl)
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

## Developer Information

[GitHub project](https://github.com/pmonks/unfurl)

[Bug Tracker](https://github.com/pmonks/unfurl/issues)

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
