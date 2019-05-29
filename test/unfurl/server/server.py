from flask import Flask, make_response
from flask_headers import headers
app = Flask(__name__)

@app.route("/")
@headers({'Access-Control-Allow-Origin': '*'})
def home():
    response = make_response("hola!")
    return response

def get_facebook():
    file = open('facebook.html')
    body = file.read()
    file.close()
    response = make_response(body)
    response.headers = {
        'Cache-Control': 'private, no-cache, no-store, must-revalidate',
        'Connection': 'keep-alive',
        'Content-Type': 'text/html; charset="utf-8"',
        'Date': 'Thu, 23 May 2019 13:42:01 GMT',
        'Expires': 'Sat, 01 Jan 2000 00:00:00 GMT',
        'Pragma': 'no-cache',
        'Set-Cookie': 'fr=1rnK14DXebf2n91Zp..Bc5qMp.gh.AAA.0.0.Bc5qMp.AWWcEJao; expires=Fri, 22-May-2020 13:42:01 GMT; Max-Age=31536000; path=/; domain=.facebook.com; secure; httponly',
        'Set-Cookie': 'sb=KaPmXFL0kwcuYHHs8x2OnIlj; expires=Sat, 22-May-2021 13:42:01 GMT; Max-Age=63072000; path=/; domain=.facebook.com; secure; httponly',
        'Strict-Transport-Security': 'max-age=15552000; preload',
        'Vary': 'Accept-Encoding',
        'X-Content-Type-Options': 'nosniff',
        'X-FB-Debug': 'P2ShNmp3KOuEWmeuWhfDU66OyvaO2lw7hNlvGM+eO/PIPps2RppYRk04O0SEGSv5pOzc/qqpziMUBaZKWKTZmw==',
        'X-Frame-Options': 'DENY',
        'X-XSS-Protection': 0,
    }
    return response

@app.route("/facebook-no-cors")
def facebook_no_cors():
    return get_facebook()

@app.route("/text")
@headers({'Access-Control-Allow-Origin': '*'})
def bad_content_type():
    response = make_response("hola!")
    response.headers['Content-Type'] = 'text/plain'
    return response

@app.route("/facebook")
@headers({'Access-Control-Allow-Origin': '*'})
def facebook_cors():
    response = get_facebook()
    return response

@app.route("/clojure")
@headers({'Access-Control-Allow-Origin': '*'})
def clojure():
    file = open('clojure.html')
    body = file.read()
    file.close()
    response = make_response(body)
    response.headers = {
        'Age': 61321,
        'Connection': 'keep-alive',
        'Content-Type': 'text/html',
        'Date': 'Thu, 23 May 2019 01:11:20 GMT',
        'Last-Modified': 'Thu, 23 May 2019 01:07:55 GMT',
        'Server': 'AmazonS3',
        'Vary': 'Accept-Encoding',
        'Via': '1.1 429fb4e05d6db25afd75d7eb9f5fa85d.cloudfront.net (CloudFront)',
        'X-Amz-Cf-Id': 'dSeCoHUVE2ekTG9E_irlmbgJvGxBbd5q06dFaL7DjCNUtwF0evGsiw==',
        'X-Cache': 'Hit from cloudfront',
    }
    return response

@app.route("/techcrunch")
@headers({'Access-Control-Allow-Origin': '*'})
def techcrunch():
    file = open('techcrunch.html')
    body = file.read()
    file.close()
    response = make_response(body)
    response.headers = {
        'Accept-Ranges': 'bytes',
        'Age': 135,
        'Cache-Control': 'max-age=300, must-revalidate',
        'Connection': 'keep-alive',
        'Content-Length': 66704,
        'Content-Security-Policy': 'default-src https: \'unsafe-inline\' \'unsafe-eval\'; img-src https: data:; object-src \'none\'; worker-src \'self\'; upgrade-insecure-requests; block-all-mixed-content; disown-opener; sandbox allow-forms allow-same-origin allow-scripts allow-popups allow-popups-to-escape-sandbox allow-presentation;',
        'Content-Type': 'text/html; charset=UTF-8',
        'Date': 'Thu, 23 May 2019 18:20:49 GMT',
        'Link': '<https://techcrunch.com/wp-json/>; rel="https://api.w.org/"',
        'Link': '<https://techcrunch.com/?p=1382516>; rel=shortlink',
        'Server': 'nginx',
        'Strict-Transport-Security': 'max-age=31536000;',
        'Vary': 'Accept-Encoding',
        'X-Cache': 'hit',
        'X-Powered-By': 'WordPress.com VIP <https://vip.wordpress.com>',
        'X-hacker': 'If you\'re reading this, you should visit automattic.com/jobs and apply to join the fun, mention this header.',
        'X-rq': 'mia1 102 143 3207',
    }
    return response

@app.route("/linkedin")
@headers({'Access-Control-Allow-Origin': '*'})
def linkedin():
    file = open('linkedin.html')
    body = file.read()
    file.close()
    response = make_response(body)
    response.headers = {
        'Content-Length': 1461,
        'Content-Type': 'text/html',
        'Date': 'Thu, 23 May 2019 19:07:16 GMT',
        'Set-Cookie': 'lidc="b=TGST04:g=1548:u=1:i=1558638436:t=1558724836:s=AQF9mnX0fQA6qV8J2u-9zWIk6ZMUpvYs"; Expires=Fri, 24 May 2019 19:07:16 GMT; domain=.linkedin.com; Path=/',
        'Set-Cookie': 'trkCode=gf; Max-Age=5',
        'Set-Cookie': 'trkInfo=AQEHgAUMhhplrgAAAWrmFx6gamgpOfppEO-SZn3Gg4xtXgBBCG9IC8sH6mvRReSfduEQp-7ghVRoLb6HxU6vobNWY12flfiROJB0j_kAGbNu3-Zcnt6_e7oK96Rch1Zxq0rLlBQ=; Max-Age=5',
        'Set-Cookie': 'rtc=AQHT_480SoyQnwAAAWrmFx6gNzdsBtfZ-2hXfbffOOHzJd7JthPtkkgxC1hi4mpBKq65hFYl3LvYVzO7yFzESFMvTdHsn5emu3_KONvkUHgpjuLfnT0rDtXeT1mFXIoy5lJzbKvhoANpgzKNM7DFUSorajSyd_fOgAIV2Czcr9D7mU0qAZi2qQLmMAP2zPaEb1kCA00wLpaLxuZQWfWN77E6USID3nfbA2rd3xF5z9Xo11sXbrnudcpZYSOSxBi6FA_S7XSy-fSRhRsXZ3bWvG2HpsoZwcKIgGZ3_83zKRoiwlKqCrcZpeX24NOMgqNmW73wzbyfgBYD-GtFpoJWAoM=; Max-Age=120; path=/; domain=.linkedin.com',
        'X-LI-Proto': 'http/1.1',
        'X-LI-UUID': 'rjJaaGZloRVAuzITOSsAAA==',
        'X-Li-Pop': 'prod-eda6',
    }
    return response

app.run(port=8888)
