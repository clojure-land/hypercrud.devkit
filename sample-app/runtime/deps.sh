#!/usr/bin/env bash
set -eux -o pipefail

yarn
pushd resources-node && yarn && popd
pushd vendor/promesa && lein install && popd
boot show -d
