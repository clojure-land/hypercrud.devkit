## Service
#### Build
    $ boot build
#### Run
    $ java -jar target/project.jar
Confirm in your browser: http://localhost:8080/

    Hypercrud Server Running!


## Runtime
#### Build
    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot browser
#### Run
    $ cd target/browser
    $ python -m SimpleHTTPServer

View in your browser: http://localhost:8000/
