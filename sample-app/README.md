
## System Requirements
* [Boot](https://github.com/boot-clj/boot#install)
* [Java]()
* [yarn](https://yarnpkg.com/en/docs/install)
* [~~Node~~](https://nodejs.org/)

## Service
#### Build
    $ boot build
#### Run
    $ java -jar target/project.jar TRANSACTOR_URI

## Runtime
#### Build
    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot browser
#### Run
    $ cd target/browser
    $ python -m SimpleHTTPServer
