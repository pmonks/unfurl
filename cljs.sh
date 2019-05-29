#! /bin/bash

sudo npm install -g karma karma-cljs-test karma-firefox-launcher
sudo pip3 install flask flask-headers
python test/unfurl/server/server.py &
lein cljsbuild test
