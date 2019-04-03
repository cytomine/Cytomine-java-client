package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2019. Authors: see NOTICE file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;

import java.util.List;

public class UploadedFile extends Model<UploadedFile> {

    public enum Status {
        UPLOADED (0),

        DETECTING_FORMAT (10),
        ERROR_FORMAT (11), // 3

        EXTRACTING_DATA (20),
        ERROR_EXTRACTION (21),

        CONVERTING (30),
        ERROR_CONVERSION (31), // 4

        DEPLOYING (40),
        ERROR_DEPLOYMENT (41), // 8

        DEPLOYED (100),
        EXTRACTED (102),
        CONVERTED (104);

        private final int code;
        Status(int code) {
            this.code = code;
        }
        public int getCode() { return code; }
    }

    public UploadedFile(){}
    public UploadedFile(ImageServer server, String originalFilename, String realFilename, Long size, String ext,
                        String contentType, Collection<Project> projects, Storage storage, User user, Status status,
                        UploadedFile parent){
        this(server.getId(), originalFilename, realFilename, size, ext, contentType, projects.getListIds(), storage.getId(),
                user.getId(), status, parent != null ? parent.getId() : null);
    }
    public UploadedFile(Long idImageServer, String originalFilename, String realFilename, Long size, String ext,
                        String contentType, List idProjects, Long idStorage, Long idUser, Status status, Long idParent){
        this.set("originalFilename", originalFilename);
        this.set("filename", realFilename);
        this.set("size", size);

        this.set("ext", ext);
        this.set("contentType", contentType);

        this.set("projects", idProjects);
        this.set("storage", idStorage);

        this.set("user", idUser);
        this.set("imageServer", idImageServer);

        this.set("parent", idParent);
        if (status != null) {
            this.set("status", status.code);
        }
    }

    public UploadedFile changeStatus(Status status) throws CytomineException {
        this.set("status", status.code);
        return this.update();
    }
}
