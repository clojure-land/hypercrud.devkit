# hello-world

Like most web apps, hello-world is split into two modules,
a [service](#service) layer for storing and accessing application data and a [ui runtime](#runtime).

## Service

    boot build
    java -jar target/project.jar

Confirm in browser: http://localhost:8080/

    Hypercrud Server Running!

## Runtime

    yarn
    NODE_ENV=production node_modules/.bin/webpack
    boot build     # artifacts in `target`

    cd target
    python -m SimpleHTTPServer 8000

View in your browser: http://localhost:8000/
