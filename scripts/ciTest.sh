#!/bin/bash

set -o xtrace
set -o errexit

echo "************************************** Launch tests ******************************************"

file='./ci/version'
VERSION_NUMBER=$(<"$file")

echo "Launch tests for $VERSION_NUMBER"

docker build --rm -f scripts/docker/Dockerfile-test.build --build-arg VERSION_NUMBER=$VERSION_NUMBER -t  cytomine/cytomine-java-test .
mkdir ./ci/surefire-reports
containerId=$(docker create --network scripts_default --link nginxTest:localhost-core -v "$PWD"/ci/surefire-reports:/app/target/surefire-reports cytomine/cytomine-java-test )
#docker network connect scripts_default $containerId
docker start -ai  $containerId
docker rm $containerId