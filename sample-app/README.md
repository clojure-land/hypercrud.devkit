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

TODO description

#### Build & Run
From the runtime directory, execute the following to compile the browser build:

    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot browser

Then execute the following to build the node SSR service

    $ boot node

Then start the node server:

    $ cd target/node
    $ yarn
    $ node preamble.js

View in your browser: http://localhost:3000/
