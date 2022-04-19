package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2022. Authors: see NOTICE file.
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
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * Upload an image on the plateform
     *
     * @param uploadURL    The related Cytomine upload URL (distinct to the Core URL)
     * @param file                 The image file path
     * @param synchrone            If true, the response will be send from server when the image will be converted, transfered, created,...(May take a long time)
     *                             Otherwise the server response directly after getting the image and the parameters
     * @return The created uploadedFile
     * @throws Exception Error during upload
     */
    public UploadedFile upload(String uploadURL,  String file, boolean synchrone) throws CytomineException {
        return upload(uploadURL, file, (Map)null, synchrone);
    }
    public UploadedFile upload(String uploadURL,  String file, Long idStorage, boolean synchrone) throws CytomineException {
        return upload(uploadURL, file, 0L, idStorage, null, synchrone);
    }
    public UploadedFile upload(String uploadURL,  String file, Long idProject, Long idStorage, boolean synchrone) throws CytomineException {
        return upload(uploadURL, file, idProject, idStorage, null, synchrone);
    }
    public UploadedFile upload(String uploadURL,  String file, Map<String, String> properties, boolean synchrone) throws CytomineException {
        Storage s = Storage.getCurrentUserStorage();
        return upload(uploadURL, file, 0L, s.getId(), properties, synchrone);
    }
    /**
     * Upload an image on the plateform
     *
     * @param uploadURL    The related Cytomine upload URL (distinct to the Core URL)
     * @param file         The image file absolute path
     * @param idProject    If not null, add the image in this project
     * @param idStorage    The storage where the image will be copied
     * @param properties   These key-value will be add to the generated AbstractImage as Property domain instance
     * @param synchrone    If true, the response will be send from server when the image will be converted, transfered, created,...(May take a long time)
     *                             Otherwise the server response directly after getting the image and the parameters
     * @return The created uploadedFile
     * @throws Exception Error during upload
     */
    public UploadedFile upload(String uploadURL, String file, Long idProject, Long idStorage, Map<String, String> properties, boolean synchrone) throws CytomineException {

        CytomineConnection uploadConnection = Cytomine.connection(
                uploadURL,
                Cytomine.getInstance().getDefaultCytomineConnection().getPublicKey(),
                Cytomine.getInstance().getDefaultCytomineConnection().getPrivateKey(),
                false);

        Map<String, String> entityParts = new HashMap<>();
        if (idProject != null) {
            entityParts.put("idProject", idProject + "");
        }
        if (idStorage != null) {
            entityParts.put("idStorage", idStorage + "");
        }

        String projectParam = "";
        if (idProject != null && idProject != 0l) {
            projectParam = "&idProject=" + idProject;
        }

        String cytomineHost = Cytomine.getInstance().getDefaultCytomineConnection().getHost();
        String url = "/upload?idStorage=" + idStorage + "&cytomine=" + cytomineHost + projectParam;
        if (properties != null && properties.size() > 0) {
            List<String> keys = new ArrayList<String>();
            List<String> values = new ArrayList<String>();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                keys.add(entry.getKey());
                values.add(entry.getValue());
            }
            url = url + "&keys=" + StringUtils.join(keys, ",") + "&values=" + StringUtils.join(values, ",");
        }
        if (synchrone) {
            url = url + "&sync=" + true;
        }

        JSONObject uploadResult = (JSONObject) uploadConnection.uploadImage(file, url, entityParts ).get(0);
        uploadResult = (JSONObject) uploadResult.get("uploadFile");
        this.setAttr((JSONObject) uploadResult.get("attr"));
        return this;
    }


    /*public String getAbsolutePath() {
        return this.get("path") + File.separator + this.get("filename");
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
    }*/

}
