| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/pmonks/unfurl/tree/main) | [![CI](https://github.com/pmonks/unfurl/workflows/CI/badge.svg?branch=main)](https://github.com/pmonks/unfurl/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/pmonks/unfurl/workflows/dependencies/badge.svg?branch=main)](https://github.com/pmonks/unfurl/actions?query=workflow%3Adependencies) |
| [**dev**](https://github.com/pmonks/unfurl/tree/dev)  | [![CI](https://github.com/pmonks/unfurl/workflows/CI/badge.svg?branch=dev)](https://github.com/pmonks/unfurl/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/pmonks/unfurl/workflows/dependencies/badge.svg?branch=dev)](https://github.com/pmonks/unfurl/actions?query=workflow%3Adependencies) |

[![Latest Version](https://img.shields.io/clojars/v/com.github.pmonks/unfurl)](https://clojars.org/com.github.pmonks/unfurl/) [![Open Issues](https://img.shields.io/github/issues/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/issues) [![License](https://img.shields.io/github/license/pmonks/unfurl.svg)](https://github.com/pmonks/unfurl/blob/main/LICENSE)

# unfurl

This library implements "URL unfurling" approximately according to how Slack does it.
See [this blog post](https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs)
for more info.

## Installation

unfurl is available as a Maven artifact from [Clojars](https://clojars.org/com.github.pmonks/unfurl).  The latest version is:

### Trying it Out

#### Clojure CLI

```shell
$ clj -Sdeps '{:deps {com.github.pmonks/unfurl {:mvn/version "#.#.#"}}}'  # Where #.#.# is replaced with an actual version number >= 0.10.0
```

#### Leiningen

```shell
$ lein try com.github.pmonks/unfurl
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

The library provides a single function - `unfurl`.  [The API documentation](https://pmonks.github.io/unfurl/) has full details, and [the unit tests](https://github.com/pmonks/unfurl/blob/master/test/unfurl/api_test.clj) provide some example usages.

## Contributor Information

[Contributing Guidelines](https://github.com/pmonks/unfurl/blob/main/.github/CONTRIBUTING.md)

[Bug Tracker](https://github.com/pmonks/unfurl/issues)

[Code of Conduct](https://github.com/pmonks/unfurl/blob/main/.github/CODE_OF_CONDUCT.md)

### Developer Workflow

The repository has two permanent branches: `main` and `dev`.  **All development must occur either in branch `dev`, or (preferably) in feature branches off of `dev`.**  All PRs must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

This model allows otherwise unrelated changes to be batched up in the `dev` branch, integration tested there, and then released en masse to the `main` branch, which will trigger automated generation and deployment of the release (Codox docs to GitHub Pages, JARs to Clojars, etc.).

### Why are there so many different groupIds on Clojars for this project?

The project was originally developed under my personal GitHub account.  In early 2018 it was transferred to the `clj-commons` GitHub organisation, but then, as that group refined their scope and mission, it was determined that it no longer belonged there, and the project were transferred back in late 2021.  During this time the build tooling for the project also changed from Leiningen to tools.build, which created further groupId churn (tools.build introduced special, useful semantics for `com.github.username` groupIds that don't exist with Leiningen or Clojars).

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
