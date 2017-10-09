# Hypercrud Devkit
> Browser, Node and JVM scaffolding for selfhost.

Fork this project to selfhost a hyperfiddle, or to use the Hypercrud I/O layer directly.

* Nodejs entrypoint with server side rendering
* Browser entrypoint with HTML5 navigation & router
* JVM entrypoint with Pedestal server and Datomic

Use this pattern for production infrastructure stuff when you need to do things like analytics, http headers, auth integrations, custom service security, really anything that you want to do at a lower layer than the data driven UI.

## Library or framework?

Hypercrud is a *ClojureScript library*, you compose values and simple functions. No macros, interfaces, dependency injection or anything like that.

There is also a tiny Clojure piece, 2-300 loc that wraps Datomic API. It's mostly historical and predates the Datomic Client API and it's probably possible to just replace with a Datomic Client. There is also an example Pedestal service which you can use or ignore.


# How to run

1. Datomic - download and run
2. Web service - build and run
3. Node server - build and run

## Datomic

Get Datomic Free here: https://my.datomic.com/downloads/free

    wget --output-document=datomic.zip https://my.datomic.com/downloads/free/0.9.5561.62
    unzip datomic.zip
    cd datomic-free-0.9.5561.62
    bin/transactor -Xmx1024m -Xms256m `pwd`/config/samples/free-transactor-template.properties
    
You should see:
    
    System started datomic:free://localhost:4334/<DB-NAME>, storing data in: data

## JVM web service

    boot build
    java -jar target/service-0.0.1-SNAPSHOT.jar
    
You should see:

    Serving

Confirm in browser: http://localhost:8081/

    Hypercrud Server Running!

## Node web server and browser assets

    yarn
    NODE_ENV=production node_modules/.bin/webpack
    boot browser
    boot node

    cd target/node
    yarn
    node preamble.js

Confirm in your browser: http://localhost:8080/

#### Cursive needs a generated leiningen project

Before you launch Cursive, do this:

    pushd service && boot show -d && popd
    pushd runtime && boot show -d && popd

If Cursive prompts you, **Don't click 'Add Source Root'!!!** You need to "File > Project Structure > Import Modules". IntelliJ doesn't make this simple.
