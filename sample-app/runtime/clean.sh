#!/usr/bin/env bash
set -eux -o pipefail

rm -rf node_modules &
rm -rf generated-resources-browser &
rm -rf target &
wait
