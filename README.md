# Hypercrud Devkit

This repo contains two independent projects.

* [hello-world](hello-world)
* [sample-app](sample-app)

Hello world is the simplest possible hypercrud project. It's just for understanding, you probably won't use this as a template.

Sample-app is a more use interesting hypercrud project with

* Data driven UI
* including popovers, branches, staging area
* Node server side rendering
* HTML5 navigation & router

This is a good template for real-world hypercrud apps, use this pattern to do prod infrastructure and integrations like the following:

* User login and OAuth
* Custom service security
* analytics
* localstorage
* environment variables
* caching/CDN integration
* database transactions from node

## System Requirements
* [Boot](https://github.com/boot-clj/boot#install)
* [Java]()
* [yarn](https://yarnpkg.com/en/docs/install)
* [Node](https://nodejs.org/)

## How to set up Cursive

First generate the leiningen projects for Cursive, in both projects:

    # Generates leiningen project for Cursive to import
    pushd service && boot show -d && popd
    pushd runtime && boot show -d && popd

When Cursive prompts, **Don't click the Add Source Root!!!** You need to "File > Project Structure > Import Modules".
