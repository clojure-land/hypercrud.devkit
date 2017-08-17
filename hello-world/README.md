# hello-world

Like most web apps, hello-world is split into two modules,
a [service](#service) layer for storing and accessing application data and a [ui runtime](#runtime).

## Service
This service layer runs the [Hypercrud HTTP API](https://github.com/hyperfiddle/hypercrud.server) on top of a [pedestal server](https://github.com/pedestal/pedestal),
reading and writing to an in-memory [datomic](http://www.datomic.com/) database.

There are only two files:
* `load.clj` to properly seed a datomic database with the example data
* `main.clj` to coordinate the bootstrapping of pedestal server with the Hypercrud HTTP API and datomic

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
From the runtime directory, execute the following:

    $ yarn
    $ NODE_ENV=production node_modules/.bin/webpack
    $ boot build

Then serve the build directory, `target`, e.g.:

    $ cd target
    $ python -m SimpleHTTPServer

View in your browser: http://localhost:8000/
