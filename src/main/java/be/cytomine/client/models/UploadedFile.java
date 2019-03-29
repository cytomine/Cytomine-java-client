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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.List;

public class UploadedFile extends Model<UploadedFile> {

    public enum Status {

        UPLOADED (0),
        CONVERTED (1),
        DEPLOYED (2),
        ERROR_FORMAT (3),
        ERROR_CONVERSION (4),
        UNCOMPRESSED (5),
        TO_DEPLOY (6),
        TO_CONVERT (7),
        ERROR_DEPLOYMENT (8);

        private final int code;
        Status(int code) {
            this.code = code;
        }
        public int getCode() { return code; }
    }

    public UploadedFile(){}
    public UploadedFile(String originalFilename, String realFilename, File file, Long size, String ext, String contentType, List idProjects, List idStorages, User user, Status status, UploadedFile parent){
        this(originalFilename, realFilename, file.getAbsolutePath(), size, ext, contentType, idProjects, idStorages, user.getId(), status, parent != null ? parent.getId() : null);
    }
    public UploadedFile(String originalFilename, String realFilename, String path, Long size, String ext, String contentType, List idProjects, List idStorages, Long idUser, Status status, Long idParent){
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
        if (status != null) {
            this.set("status", status.code);
        }
    }

    public AttachedFile uploadAttachedFile(String file, String domainClassName, Long domainIdent) throws CytomineException {
        String url = "/api/attachedfile.json?domainClassName=" + domainClassName + "&domainIdent=" + domainIdent;
        JSONObject json = Cytomine.getInstance().getDefaultCytomineConnection().uploadFile(url, file);
        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setAttr(json);
        return attachedFile;
    }

    public String getAbsolutePath() {
        return this.get("path") + File.separator + this.get("filename");
    }

    public UploadedFile changeStatus(Status status) throws CytomineException {
        this.set("status", status.code);
        return this.update();
    }

    public static UploadedFile getByAbstractImage(AbstractImage ai) throws CytomineException {
        return getByAbstractImage(ai.getId());
    }
    public static UploadedFile getByAbstractImage(Long idAbstractImage) throws CytomineException {
        UploadedFile uf = new UploadedFile();
        uf.addFilter("image", idAbstractImage.toString());
        return uf.fetch(null);
    }

    @Override
    public String getJSONResourceURL() {
        //TODO change when rest url are normalized
        if(isFilterBy("image")) return "/api/" + getDomainName() + "/image/"+getFilter("image")+".json";

        return super.getJSONResourceURL();
    }

}
