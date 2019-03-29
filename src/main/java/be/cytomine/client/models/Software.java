package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2018. Authors: see NOTICE file.
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

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class Software extends Model<Software> {
    public Software(){}
    public Software(String name, String serviceName, String resultType, String executeCommand){
        this.set("name", name);
        this.set("serviceName", serviceName);
        this.set("resultName", resultType);
        this.set("executeCommand", executeCommand);
    }
    public Software deprecateSoftware(Long id) throws CytomineException {
        Software software = this.fetch(id) ;
        software.set("deprecated", true);
        return update();
    }

    public Software addSoftware(String softwareVersion, String name, Long idSoftwareUserRepository, Long idDefaultProcessingServer, String resultType, String executeCommand, String pullingCommand) throws CytomineException {
        Software software = new Software();
        software.set("softwareVersion", softwareVersion);
        software.set("name", name);
        software.set("softwareUserRepository", idSoftwareUserRepository);
        software.set("defaultProcessingServer", idDefaultProcessingServer);
        software.set("resultName", resultType);
        software.set("executeCommand", executeCommand);
        software.set("pullingCommand", pullingCommand);
        return software.save();
    }
}
