# hello-world

Like most web apps, hello-world is split into two modules,
a [service](#service) layer for storing and accessing application data and a [ui runtime](#runtime).

## Service

From service directory:

    boot build
    java -jar target/project.jar

Confirm success in your browser: http://localhost:8080/

    Hypercrud Server Running!

## Runtime

    yarn
    NODE_ENV=production node_modules/.bin/webpack
    boot build

Browser artifacts are in `target`

    cd target
    python -m SimpleHTTPServer 8000

View in your browser: http://localhost:8000/

Observe HTTP POST to /hydrate with request body and response.