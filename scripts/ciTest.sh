#!/bin/bash

set -o xtrace
set -o errexit

echo "************************************** Launch tests ******************************************"

file='./ci/version'
VERSION_NUMBER=$(<"$file")

echo "Launch tests for $VERSION_NUMBER"

docker build --rm -f scripts/docker/Dockerfile-test.build -t cytomine/cytomine-java-client-test \
        --build-arg VERSION_NUMBER=$VERSION_NUMBER  .

containerId=$(docker create cytomine/cytomine-java-client-test)

docker rm $containerId