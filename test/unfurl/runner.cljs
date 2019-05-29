(ns unfurl.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-all-tests doo-tests]]
            [unfurl.api-test]))

(doo-tests 'unfurl.api-test)
