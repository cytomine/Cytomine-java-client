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

import java.io.File;

public class Software extends Model<Software> {
    public Software(){}
    public Software(String name, String resultType, String executeCommand, String softwareVersion){
        this.set("name", name);
        this.set("resultName", resultType);
        this.set("executeCommand", executeCommand);
        this.set("softwareVersion", softwareVersion);
    }

    public Software(String name, String resultType, String executeCommand, String softwareVersion, Long idSoftwareUserRepository, Long idDefaultProcessingServer){
        this.set("name", name);
        this.set("softwareUserRepository", idSoftwareUserRepository);
        this.set("defaultProcessingServer", idDefaultProcessingServer);
        this.set("resultName", resultType);
        this.set("executeCommand", executeCommand);
        this.set("softwareVersion", softwareVersion);
    }

    public Software(String name, String resultType, String executeCommand, String softwareVersion, Long idSoftwareUserRepository, Long idDefaultProcessingServer, String pullingCommand){
        this.set("name", name);
        this.set("softwareUserRepository", idSoftwareUserRepository);
        this.set("defaultProcessingServer", idDefaultProcessingServer);
        this.set("resultName", resultType);
        this.set("executeCommand", executeCommand);
        this.set("softwareVersion", softwareVersion);
        this.set("pullingCommand", pullingCommand);
    }

    public Software(Boolean deprecated , String softwareVersion, String name, Long idSoftwareUserRepository, Long idDefaultProcessingServer, String resultType, String executeCommand, String pullingCommand){

        this.set("softwareVersion", softwareVersion);
        this.set("name", name);
        this.set("softwareUserRepository", idSoftwareUserRepository);
        this.set("defaultProcessingServer", idDefaultProcessingServer);
        this.set("resultName", resultType);
        this.set("executeCommand", executeCommand);
        this.set("pullingCommand", pullingCommand);
        this.set("deprecated", deprecated);
    }

    public Software deprecate() throws CytomineException {
        this.set("deprecated", true);
        return this.update();
    }

    public void upload(File file) throws CytomineException {
        upload(Cytomine.getInstance().getDefaultCytomineConnection(), file);
    }
    public void upload(CytomineConnection connection, File file) throws CytomineException {
        String url = "/api/"+ getDomainName()+"/upload";
        connection.uploadFile(url, file, attr);
    }

    public void download(String destPath) throws CytomineException {
        download(new File(destPath));
    }
    public void download(File dest) throws CytomineException {
        download(Cytomine.getInstance().getDefaultCytomineConnection(), dest);
    }
    public void download(CytomineConnection connection, File dest) throws CytomineException {
        String url = "/api/"+ getDomainName()+"/"+this.getId()+"/download";
        connection.downloadFile(url, dest.getPath());
    }

}
