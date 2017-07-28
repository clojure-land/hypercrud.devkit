#!/usr/bin/env bash
set -eux -o pipefail

NODE_ENV=production node_modules/.bin/webpack
boot node

[[ -f ./target/node/preamble.js ]] || exit 1

# should be `-b prod` but that's broken right now
boot browser -b dev
