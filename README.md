# Hypercrud Devkit

Fork this project if you'd like to selfhost your hyperfiddle and have full control.

This project shows you how to

* Full control over CLJS and CLJ entrypoints
* Connect to Datomic
* Call into data driven UI functions
* Node server side rendering
* HTML5 navigation & router

Use this pattern for production infrastructure stuff when you need to do things like analytics, http headers, auth integrations, custom service security, really anything that you want to do at a lower layer than the data driven UI.

## Service

    boot build
    java -jar target/project.jar

Confirm in browser: http://localhost:8080/

    Hypercrud Server Running!

## Runtime

    yarn
    NODE_ENV=production node_modules/.bin/webpack
    boot browser     # web assets
    boot node        # node server and assets

    cd target/node
    yarn
    node preamble.js

View in your browser: http://localhost:3000/

## Cursive needs a generated leiningen project

Before you launch Cursive, do this:

    pushd service && boot show -d && popd
    pushd runtime && boot show -d && popd

If Cursive prompts you, **Don't click 'Add Source Root'!!!** You need to "File > Project Structure > Import Modules".
