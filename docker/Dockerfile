# Copyright (c) 2009-2022. Authors: see NOTICE file.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ARG BASE_IMAGE
ARG BASE_TAG
FROM $BASE_IMAGE:$BASE_TAG

ARG VERSION
ARG NAMESPACE=Cytomine-ULiege
ARG RELEASE_PATH=https://github.com/${NAMESPACE}/Cytomine-java-client/releases/download/v${VERSION}
ADD ${RELEASE_PATH}/cytomine-java-client-${VERSION}-jar-with-dependencies.jar /lib/cytomine-java-client.jar
USER root
RUN chmod +x /lib/*