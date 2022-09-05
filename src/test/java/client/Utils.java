package client;

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
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.ProjectCollection;
import be.cytomine.client.collections.StorageCollection;
import be.cytomine.client.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class Utils {

    private static String host;
    private static String publicKey;
    private static String privateKey;
    private static Cytomine cytomine;
    private static final Logger log = LogManager.getLogger(Utils.class);

    private static User user;
    private static Project project;
    private static ImageInstance imageInstance;
    private static SliceInstance sliceInstance;
    private static Ontology ontology;
    private static Term term;
    private static Annotation annotation;
    private static ImageServer imageServer;
    private static UploadedFile uploadedFile;
    private static AbstractImage abstractImage;
    private static AbstractSlice abstractSlice;
    private static Tag tag;


    static void connect() throws CytomineException {
        if(cytomine != null) return;

        host="http://localhost-core";
        publicKey="4c6339f4-289a-4add-82cf-120a6a808b6f";
        privateKey="563de51e-d78c-4e07-9589-7873bd3341be";

        log.info("Connection to cytomine...");
        Cytomine.connection(host,publicKey,privateKey);
        cytomine = Cytomine.getInstance();
        cytomine.waitToAcceptConnection(120);
    }
    static String getPublicKey() {
        return publicKey;
    }

    static String getRandomString(){
        return UUID.randomUUID().toString();
    }

    static File getFile() {
        return new File("src/test/java/client/samples/text.txt");
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
        if(ontology == null) ontology = new Ontology(getRandomString()).save();
        return ontology;
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
    static Software getSoftware() throws CytomineException {
        String name = Utils.getRandomString();
        return new Software(name, "", "", "").save();
    }
    static Job getJob() throws CytomineException {
        Software software = getSoftware();
        Project project = getProject();
        return new Job(software, project).save();
    }

    static ImageInstance getImageInstance() throws CytomineException {
        if(imageInstance == null) imageInstance = getNewImageInstance();
        return imageInstance;
    }
    static ImageInstance getNewImageInstance() throws CytomineException {
        return new ImageInstance(Utils.getNewAbstractImage(), Utils.getProject()).save();
    }

    static SliceInstance getSliceInstance() throws CytomineException {
        if(sliceInstance == null) sliceInstance = getNewSliceInstance();
        return sliceInstance;
    }
    static SliceInstance  getNewSliceInstance() throws CytomineException {
        return new SliceInstance(Utils.getProject(), Utils.getImageInstance(), Utils.getAbstractSlice()).save();
    }

    static Term getTerm() throws CytomineException {
        if(term == null) term = getNewTerm();
        return term;
    }
    static Term getNewTerm() throws CytomineException {
        String name = Utils.getRandomString();
        return new Term(name,"#FF0000", Utils.getOntology()).save();
    }

    static Annotation getAnnotation() throws CytomineException {
        if(annotation == null) annotation = getNewAnnotation();
        return annotation;
    }
    static Annotation getNewAnnotation() throws CytomineException {
        SliceInstance slice = Utils.getSliceInstance();
        return new Annotation("POLYGON ((1983 2168, 2107 2160, 2047 2074, 1983 2168))", slice).save();
    }

    static ImageServer getImageServer() throws CytomineException {
        if (imageServer == null) imageServer = getNewImageServer();
        return imageServer;
    }
    static ImageServer getNewImageServer() throws CytomineException {
        Collection<ImageServer> c = Collection.fetch(ImageServer.class);
        return c.get(0);
    }

    static UploadedFile getUploadedFile() throws CytomineException {
        if (uploadedFile == null) uploadedFile = getNewUploadedFile();
        return uploadedFile;
    }
    static UploadedFile getNewUploadedFile() throws CytomineException {
        String originalFilename = Utils.getRandomString();
        String filename = Utils.getRandomString();
        return new UploadedFile(Utils.getImageServer(), originalFilename, filename, 0L, "ext", "contentType",
                new ProjectCollection(), new StorageCollection().fetch().get(0), Utils.getUser(),
                UploadedFile.Status.DEPLOYED, null).save();
    }

    static AbstractImage getAbstractImage() throws CytomineException {
        if (abstractImage == null) abstractImage = getNewAbstractImage();
        return abstractImage;
    }
    static AbstractImage getNewAbstractImage() throws CytomineException {
        String name = Utils.getRandomString();
        AbstractImage ai = new AbstractImage(Utils.getUploadedFile(), name);
        ai.set("width",3000);
        ai.set("height",3000);
        ai.save();
        return ai;
    }

    static AbstractSlice getAbstractSlice() throws CytomineException {
        if (abstractSlice == null) abstractSlice = getNewAbstractSlice();
        return abstractSlice;
    }
    static AbstractSlice getNewAbstractSlice() throws CytomineException {
        return new AbstractSlice(Utils.getAbstractImage(), Utils.getUploadedFile(), "image/pyrtiff",0,0,0).save();
    }

    static Tag getTag() throws CytomineException {
        if(tag == null) tag = getNewTag();
        return tag;
    }
    static Tag getNewTag() throws CytomineException {
        String name = Utils.getRandomString();
        return new Tag(name).save();
    }
}
