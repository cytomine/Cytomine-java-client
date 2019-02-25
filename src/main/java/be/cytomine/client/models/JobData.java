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
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class JobData extends Model<JobData> {
    public JobData(){}
    public JobData(Job job, String key, String filename){
        this(job.getId(), key, filename);
    }
    public JobData(Long idJob, String key, String filename){
        this.set("key", key);
        this.set("job", idJob);
        this.set("filename", filename);
    }

    public void uploadJobData(File file) throws CytomineException, IOException {
        uploadJobData(Cytomine.getInstance().getDefaultCytomineConnection(), file);
    }
    public void uploadJobData(CytomineConnection connection, File file) throws CytomineException, IOException {
        connection.uploadFile("/api/jobdata/" + this.getId() + "/upload", Files.readAllBytes(file.toPath()));
    }
    public void uploadJobData(byte[] data) throws CytomineException {
        uploadJobData(Cytomine.getInstance().getDefaultCytomineConnection(), data);
    }
    public void uploadJobData(CytomineConnection connection, byte[] data) throws CytomineException {
        connection.uploadFile("/api/jobdata/" + this.getId() + "/upload", data);
    }
    public File downloadJobData(String file) throws CytomineException {
        return downloadJobData(Cytomine.getInstance().getDefaultCytomineConnection(), file);
    }
    public File downloadJobData(CytomineConnection connection, String file) throws CytomineException {
        connection.downloadFile("/api/jobdata/" + this.getId() + "/download", file);
        return new File(file);
    }

}
