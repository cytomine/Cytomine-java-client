package client;

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
import be.cytomine.client.models.Ontology;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.User;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class Utils {

    private static String host;
    private static String publicKey;
    private static String privateKey;
    private static Cytomine cytomine;
    private static final Logger log = Logger.getLogger(Utils.class);

    private static User user;
    private static Project project;


    static void connect() throws CytomineException {
        if(cytomine != null) return;
        BasicConfigurator.configure();
        PropertyConfigurator.configure("log4j.properties");

        host = System.getProperty("host");
        assertNotNull(host, "host, publicKey, privateKey");
        publicKey = System.getProperty("publicKey");
        assertNotNull(publicKey, "host, publicKey, privateKey");
        privateKey = System.getProperty("privateKey");
        assertNotNull(privateKey, "host, publicKey, privateKey");

        log.info("Connection to cytomine...");
        Cytomine.connection(host,publicKey,privateKey);
        cytomine = Cytomine.getInstance();
    }
    static String getPublicKey() {
        return publicKey;
    }

    static String getRandomString(){
        return UUID.randomUUID().toString();
    }

    //Cytomine Object getters
    static User getUser() throws CytomineException {
        if(user == null) user = getNewUser();
        return user;
    }
    static User getNewUser() throws CytomineException {
        String username = Utils.getRandomString();
        return new User(username,Utils.getRandomString(),Utils.getRandomString(),"test@test.be",Utils.getRandomString()).save();
    }
    static Ontology getOntology() throws CytomineException {
        return new Ontology(getRandomString()).save();
    }
    static Project getProject() throws CytomineException {
        if(project == null){
            project = getNewProject();

            project.addMember(getNewUser());
            project.addMember(getNewUser());
            project.addMember(getNewUser(), true);
        }
        return project;
    }
    static Project getNewProject() throws CytomineException {
        return new Project(getRandomString(),getOntology()).save();
    }
}
