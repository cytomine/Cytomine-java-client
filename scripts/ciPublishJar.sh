#!/bin/bash

set -o xtrace
set -o errexit

echo "************************************** BUILD JAR ******************************************"

file='./ci/version'
VERSION_NUMBER=$(<"$file")

echo "Build jar for $VERSION_NUMBER"

echo "GPG_KEYNAME = $GPG_KEYNAME"

docker build --rm -f scripts/Dockerfile-jar.build -t cytomine/cytomine-java-client-jar-builder \
        --build-arg VERSION_NUMBER=$VERSION_NUMBER  \
        --build-arg GPG_KEYNAME=$GPG_KEYNAME  \
        --build-arg GPG_PASSPHRASE=$GPG_PASSPHRASE  \
        --build-arg OSSRH_USER=$OSSRH_USER  \
        --build-arg OSSRH_PASSWORD=$OSSRH_PASSWORD .

containerId=$(docker create cytomine/cytomine-java-client-jar-builder)

docker rm $containerId