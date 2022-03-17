FROM maven:3.6.3-jdk-8-slim AS imageWithDependencies

# We first copy the pom.xml file and the binaries stored in the source repository.
# This way, we retrieve all maven dependencies at the beginning. All these steps will be cached by Docker unless pom.xml or libs/ has been updated.
# This means that we only retrieve all dependencies if we modify the dependencies definition.

COPY ./pom.xml /app/pom.xml

RUN cd /app && \
    mvn -f /app/pom.xml -s /usr/share/maven/ref/settings-docker.xml de.qaware.maven:go-offline-maven-plugin:resolve-dependencies -B