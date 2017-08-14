## Service
#### Build
    $ boot build
#### Install Database
    $ java -jar target/selfhost-0.0.1-SNAPSHOT.jar
#### Run
    $ java -jar target/selfhost-0.0.1-SNAPSHOT.jar YOUR-TRANSACTOR-URI

## Runtime
#### Build
    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot browser
#### Run
    $ cd target/browser
    $ python -m SimpleHTTPServer
