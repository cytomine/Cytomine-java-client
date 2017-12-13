package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2016. Authors: see NOTICE file.
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

import java.io.File;
import java.util.List;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class UploadedFile extends Model<UploadedFile> {

    public UploadedFile(){}
    public UploadedFile(String originalFilename, String realFilename, String path, Long size, String ext, String contentType, List idProjects, List idStorages, Long idUser, Long status, Long idParent){
        this.set("originalFilename", originalFilename);
        this.set("filename", realFilename);

        this.set("path", path);
        this.set("size", size);

        this.set("ext", ext);
        this.set("contentType", contentType);
        this.set("path", path);

        this.set("projects", idProjects);
        this.set("storages", idStorages);

        this.set("user", idUser);

        this.set("parent", idParent);
        if (status != -1l) {
            this.set("status", status);
        }
    }

    public String getAbsolutePath() {
        return this.get("path") + File.separator + this.get("filename");
    }

}
