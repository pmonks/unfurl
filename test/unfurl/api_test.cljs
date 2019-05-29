(ns unfurl.api-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [deftest testing is async]]
            [unfurl.api :as uf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(deftest test-server
  (testing "the test server is up"
    (async done
           (go (let [response (<! (http/get "http://localhost:8888"
                                            {:with-credentials? false}))]
                 (is (= (:status response) 200))
                 (done))))))

(deftest invalid
  (testing "an invalid URL"
    (async done
           (go (let [expected
                     {:status 404, :success false, :body "NOT FOUND",
                      :trace-redirects ["invalid"
                                        "invalid"],
                      :error-code :http-error, :error-text "Not Found [404]"}
                     actual (<! (uf/unfurl "invalid"))]
                 (is (= expected (dissoc actual :headers)))
                 (done))))))

(deftest unknown
  (testing "an unknown URL"
    (async done
           (go (let [expected
                     {:status 0, :success false, :body "", :headers {},
                      :trace-redirects ["https://unkn.own"
                                        "https://unkn.own"],
                      :error-code :http-error, :error-text " [0]"}
                     actual (<! (uf/unfurl "https://unkn.own"))]
                 (is (= expected actual))
                 (done))))))

(deftest unknown-resource
  (testing "a URL that doesn't point to a resource"
    (async done
           (go (let [expected
                     {:status 0, :success false, :body "", :headers {},
                      :trace-redirects ["http://localhost:8888/what"
                                        "http://localhost:8888/what"],
                      :error-code :http-error, :error-text " [0]"}
                     actual
                     (<! (uf/unfurl "http://localhost:8888/what"))]
                 (is (= expected actual))
                 (done))))))

(deftest resist-unfurling
  (testing "a URL that resists unfurling"
    (async done
           (go (let [expected {}
                     actual
                     (<! (uf/unfurl "http://localhost:8888/linkedin"))]
                 (is (= expected actual))
                 (done))))))

(deftest bad-content-type
  (testing "an endpoint giving something else than HTML"
    (async done
           (go (let [expected nil
                     actual
                     (<! (uf/unfurl "http://localhost:8888/text"))]
                 (is (= expected actual))
                 (done))))))

(deftest no-cors
  (testing "an endpoint with CORS disabled"
    (async done
           (go (let [expected
                     {:status 0, :success false, :body "", :headers {},
                      :trace-redirects ["http://localhost:8888/facebook-no-cors"
                                        "http://localhost:8888/facebook-no-cors"],
                      :error-code :http-error, :error-text " [0]"}
                     actual
                     (<! (uf/unfurl "http://localhost:8888/facebook-no-cors"))]
                 (is (= expected actual))
                 (done))))))

(deftest clojure
  (testing "metatag-only"
    (async done
           (go (let [expected {:title "Clojure"}
                     actual (<! (uf/unfurl "http://localhost:8888/clojure"))]
                 (is (= expected actual))
                 (done))))))

(deftest facebook
  (testing "metatags plus partial OpenGraph tags"
    (async done
           (go (let [expected
                     {:title "Facebook - Inicia sesión o regístrate",
                      :description "Crea una cuenta o inicia sesión en Facebook. Conéctate con amigos, familiares y otras personas que conozcas. Comparte fotos y videos, envía mensajes y...",
                      :url "https://www.facebook.com/",
                      :preview-url "https://www.facebook.com/images/fb_icon_325x325.png"}
                     actual (<! (uf/unfurl "http://localhost:8888/facebook"))]
                 (is (= expected actual))
                 (done))))))

(deftest techcrunch
  (testing "OpenGraph, Twitter, Swifttype and Sailthru"
    (async done
           (go (let [expected
                     {:url         "http://social.techcrunch.com/2016/09/08/its-a-long-hard-road-from-idea-to-ipo/"
                      :title       "It’s a long, hard road from idea to IPO – TechCrunch"
                      :description "It may not seem it, but coming up with an idea for your startup is probably the easiest part of launching your own company. As one industry insider told me, there are a million ways to screw up that idea through poor execution, and many, many lose their way. Yet a precious few fight through [&helli…"
                      :preview-url "https://techcrunch.com/wp-content/uploads/2016/09/img_2835-1.jpg?w=533"}
                     actual (<! (uf/unfurl "http://localhost:8888/techcrunch"))]
                 (is (= expected actual))
                 (done))))))
