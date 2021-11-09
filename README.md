| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/clj-commons/unfurl/tree/main) | [![CI](https://github.com/clj-commons/unfurl/workflows/CI/badge.svg?branch=main)](https://github.com/clj-commons/unfurl/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/clj-commons/unfurl/workflows/dependencies/badge.svg?branch=main)](https://github.com/clj-commons/unfurl/actions?query=workflow%3Adependencies) |
| [**dev**](https://github.com/clj-commons/unfurl/tree/dev)  | [![CI](https://github.com/clj-commons/unfurl/workflows/CI/badge.svg?branch=dev)](https://github.com/clj-commons/unfurl/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/clj-commons/unfurl/workflows/dependencies/badge.svg?branch=dev)](https://github.com/clj-commons/unfurl/actions?query=workflow%3Adependencies) |

[![Latest Version](https://img.shields.io/clojars/v/clj-commons/unfurl)](https://clojars.org/clj-commons/unfurl/) [![Open Issues](https://img.shields.io/github/issues/clj-commons/unfurl.svg)](https://github.com/clj-commons/unfurl/issues) [![License](https://img.shields.io/github/license/clj-commons/unfurl.svg)](https://github.com/clj-commons/unfurl/blob/main/LICENSE)

# unfurl

This library implements "URL unfurling" approximately according to how Slack does it.
See [this blog post](https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs)
for more info.

## Installation

unfurl is available as a Maven artifact from [Clojars](https://clojars.org/clj-commons/unfurl).  The latest version is:

### Trying it Out

#### Clojure CLI

```shell
$ clj -Sdeps '{:deps {clj-commons/unfurl {:mvn/version "#.#.#"}}}'  # Where #.#.# is replaced with an actual version number >= 0.10.0
```

#### Leiningen

```shell
$ lein try clj-commons/unfurl
```

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

The library provides a single function - `unfurl`.  [The API documentation](https://clj-commons.github.io/unfurl/) has full details, and [the unit tests](https://github.com/clj-commons/unfurl/blob/master/test/unfurl/api_test.clj) provide some example usages.

### API Documentation

[API documentation is available here](http://clj-commons.org/unfurl/).

## Contributor Information

[Contributing Guidelines](https://github.com/clj-commons/unfurl/blob/main/.github/CONTRIBUTING.md)

[Bug Tracker](https://github.com/clj-commons/unfurl/issues)

[Code of Conduct](https://github.com/clj-commons/unfurl/blob/main/.github/CODE_OF_CONDUCT.md)

### Developer Workflow

The repository has two permanent branches: `main` and `dev`.  **All development must occur either in branch `dev`, or (preferably) in feature branches off of `dev`.**  All PRs must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

This model allows otherwise unrelated changes to be batched up in the `dev` branch, integration tested there, and then released en masse to the `main` branch, which will trigger automated generation and deployment of the release (Codox docs to GitHub Pages, JARs to Clojars, etc.).

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
