package functional;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.SoftwareCollection;
import be.cytomine.client.models.*;
import be.cytomine.client.sample.SoftwareExample;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class FunctionalTest {

    private static final Logger log = Logger.getLogger(FunctionalTest.class);
    private static String host;
    private static String publicKey;
    private static String privateKey;
    private static Cytomine cytomine;

    private static Project project;
    private static ImageInstance image;
    private static Term term;
    @BeforeAll
    static void initAll() throws CytomineException {
        BasicConfigurator.configure();
        PropertyConfigurator.configure("log4j.properties");

        host = System.getProperty("host");
        assertNotNull(host, "host, publicKey, privateKey");
        publicKey = System.getProperty("publicKey");
        assertNotNull(publicKey, "host, publicKey, privateKey");
        privateKey = System.getProperty("privateKey");
        assertNotNull(privateKey, "host, publicKey, privateKey");

        log.info("Connection to cytomine...");
        cytomine = new Cytomine(host,publicKey,privateKey);

        Ontology ontology = cytomine.addOntology(UUID.randomUUID().toString());
        project = cytomine.addProject(UUID.randomUUID().toString(),ontology.getId());
        term = cytomine.addTerm("testTerm","#000000",ontology.getId());
    }

    @BeforeEach
    void init() {
    }

    @Test
    void getCurrentUser() {
        try {
            log.info("getCurrentUser");
            assertEquals(cytomine.getCurrentUser().get("username"), "admin", "Not the expected user");
        } catch (CytomineException e) {
            e.printStackTrace();
        }
    }

    @Test
    void upload() throws CytomineException {
        if(image != null) return;
        log.info("upload");

        assert cytomine.getStorages().size() > 0;
        Storage storage = cytomine.getStorages().get(0);

        int nbImages = cytomine.getImageInstances(project.getId()).size();
        assert nbImages == 0;

        Cytomine cytomineUpload = new Cytomine(System.getProperty("uploadUrl"), publicKey, privateKey);

        JSONArray response = cytomineUpload.uploadImage(System.getProperty("imgPath"), project.getId(), storage.getId(), host, null, true);

        assert response.size() == 1;
        assert response.get(0) instanceof JSONObject;

        JSONObject test = (JSONObject) response.get(0);
        assert test.get("status") instanceof Long;
        assert (Long) test.get("status") == 200;

        nbImages = cytomine.getImageInstances(project.getId()).size();
        assert nbImages == 1;
        image = cytomine.getImageInstances(project.getId()).get(0);
    }

    @Test
    void retrieval() throws CytomineException {
        if(image == null) upload();

        log.info("retrieval");

        Long width = Long.parseLong(image.get("width").toString());
        Long height = Long.parseLong(image.get("height").toString());

        String polygon = "POLYGON (("+((width/2)-20)+" "+((height/2)-20)+", "+((width/2)-20)+" "+((height/2)+20)+", "+((width/2)+20)+" "+((height/2)+20)+", "+((width/2)+20)+" "+((height/2)-20)+", "+((width/2)-20)+" "+((height/2)-20)+"))";

        List<Long> terms = new ArrayList<>();
        terms.add(term.getId());

        Annotation annot = cytomine.addAnnotationWithTerms(polygon, image.getId(),terms);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Annotation annot2 = cytomine.addAnnotation(polygon, image.getId());

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject response = cytomine.getSimilaritiesByRetrieval(annot2.getId());

        assertEquals(((JSONObject)((JSONArray) response.get("annotation")).get(0)).get("id"), annot.getId(), "Annotation is not returned by retrieval");
        assertEquals(((JSONObject)((JSONArray) response.get("term")).get(0)).get("id"), term.getId(), "Term is not returned by retrieval");
    }

    @Test
    void runExistingSoftware() throws CytomineException {
        if(image == null) upload();

        log.info("runSoftware");

        SoftwareExample.addSoftwareComputeTermArea(cytomine);

        SoftwareCollection softwares = cytomine.getSoftwares();

        Long computeTermAreaId = null;
        Long termsParamId = null;
        Long imagesParamId = null;
        for(int i=0;i<softwares.size();i++){
            if(softwares.get(i).get("name").equals("ComputeTermArea")){
                computeTermAreaId = softwares.get(i).getId();
            }
        }
        Software software = cytomine.getSoftware(computeTermAreaId);
        JSONArray parameters = (JSONArray) software.get("parameters");
        for(int i= 0; i<parameters.size(); i++){
            JSONObject param = (JSONObject) parameters.get(i);
            if(param.get("name").equals("terms")){
                termsParamId = Long.parseLong(param.get("id").toString());
            } else if(param.get("name").equals("images")){
                imagesParamId = Long.parseLong(param.get("id").toString());
            }
        }

        cytomine.addSoftwareProject(computeTermAreaId,project.getId());
        User userJob = cytomine.addUserJob(computeTermAreaId, cytomine.getCurrentUser().getId(), project.getId(), new Date(), null);
        Job job = cytomine.getJob((Long) userJob.get("job"));

        cytomine.addJobParameter(job.getId(), termsParamId, ""+term.getId()+"");
        cytomine.addJobParameter(job.getId(), imagesParamId, ""+image.getId()+"");

        assertEquals(0L,job.get("status"),"job must not be launched yet");

        log.info("run ComputeTermArea");
        cytomine.executeJob(job.getId());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        job = cytomine.getJob((Long) userJob.get("job"));

        assertEquals(3L,job.get("status"),"job must be successful");
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }

}