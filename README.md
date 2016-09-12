# unfurl

This WIP library is intended to implement "unfurling" approximately according to
how Slack does it. See [this blog post](https://medium.com/slack-developer-blog/everything-you-ever-wanted-to-know-about-unfurling-but-were-afraid-to-ask-or-how-to-make-your-e64b4bb9254#.jhd6zdyjs)
for more info.

## Installation

For now unfurl is available in source form only, so fire up your favourite git client and get cloning!

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

The library provides a number of methods:

```
user=> (require '[unfurl.api :as symph] :reload-all)
nil
user=> (doc unfurl.api/unfurl)
-------------------------
unfurl.api/unfurl
([url & {:keys [follow-redirects timeout-ms user-agent], :or {follow-redirects true, timeout-ms 1000, user-agent "unfurl"}}])
  Unfurls the given url, throwing various exceptions if the url is invalid,
  returning nil if the given url isn't supported, or a map containing some or
  all of the following keys (all of which may not be provided, or may be nil):

  {
    :url           - The given url
    :title         - The title of the given url
    :description   - The description of the given url
    :preview-url   - The url of a preview image for the given url
  }

nil
```

## Developer Information

[GitHub project](https://github.com/pmonks/unfurl)

[Bug Tracker](https://github.com/pmonks/unfurl/issues)

## License

Copyright © 2016 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
