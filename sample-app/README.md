# sample-app

TODO description

## Service

TODO description

#### Build & Run
To get going, from the service directory, simply run

    $ boot build

and run the built jar:

    $ java -jar target/project.jar

Confirm success in your browser: http://localhost:8080/

    Hypercrud Server Running!

## Runtime
#### Build
    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot browser
#### Run
    $ cd target/browser
    $ python -m SimpleHTTPServer
