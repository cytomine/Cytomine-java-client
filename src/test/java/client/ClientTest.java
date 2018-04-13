package client;

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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.SoftwareCollection;
import be.cytomine.client.collections.StorageCollection;
import be.cytomine.client.models.*;
import be.cytomine.client.sample.SoftwareExample;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;

import javax.imageio.stream.IIOByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class ClientTest {

    private static final Logger log = Logger.getLogger(ClientTest.class);
    private static String host;
    private static String publicKey;
    private static String privateKey;
    private static Cytomine cytomine;

    private static Ontology ontology;
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
        Cytomine.connection(host,publicKey,privateKey);
        cytomine = Cytomine.getInstance();

        createDataset();
    }

    /**
     * Used to create the dataset for tests.
     * Is also a test for the save methods.
     * @throws CytomineException
     */
    static void createDataset() throws CytomineException {
        //ontology = new Ontology("ontology").save();
        ontology = new Ontology(UUID.randomUUID().toString()).save();
        //term = new Term("term1","#000000",ontology.getId()).save();
        term = new Term(UUID.randomUUID().toString(),"#000000",ontology.getId()).save();
        //Term term2 = new Term("term2","#ffffff",ontology.getId()).save();
        //project = new Project("project",ontology.getId()).save();
        project = new Project(UUID.randomUUID().toString(),ontology.getId()).save();
        StorageCollection storages = cytomine.fetchCollection(new StorageCollection(0, 1));
        ArrayList<Long> storagesId = new ArrayList<Long>(); storagesId.add(storages.get(0).getId());

        UploadedFile uFile = new UploadedFile("originalFilename", "realFilename",
                "path", 0L, "ext", "contentType", null, storagesId,
                cytomine.getCurrentUser().getId(), UploadedFile.Status.UPLOADED, null).save();
        AbstractImage abstractImg = new AbstractImage(UUID.randomUUID().toString(), "tiff").save();
        //AbstractImage abstractImg = new AbstractImage("filename", "image/tiff").save();
        image = new ImageInstance(abstractImg.getId(), project.getId()).save();
        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        ArrayList<Long> termsId = new ArrayList<Long>(); termsId.add(term.getId());
        Annotation annotation = new Annotation(polygon, image.getId(), termsId).save();
        //TODO !!
        //AnnotationTerm annotTerm = new AnnotationTerm

    }

    @BeforeEach
    void init() {
    }

    /*@Test
    void testProjects() throws CytomineException {
        log.info("test project methods");
        String name = UUID.randomUUID().toString();
        Project p = new Project(name,ontology.getId()).save();

        p = new Project().fetch(p.getId());
        assertEquals(name, p.get("name"), "fetched name not the same used for the project creation");

        p.set("name", name+"bis");
        p.update();
        assertEquals(name+"bis", p.get("name"), "Not the name used for the project update");

        p.delete();
        try {
            new Project().fetch(p.getId());
            assert false;
        } catch (CytomineException e) { }
    }*/

  /*  @Test
    void testUploadedFiles() throws CytomineException {
        log.info("test uploaded files methods");
        String path = "path";

        StorageCollection storages = cytomine.fetchCollection(new StorageCollection(0, 1));
        ArrayList<Long> storagesId = new ArrayList<Long>(); storagesId.add(storages.get(0).getId());
        UploadedFile uf = new UploadedFile("originalFilename", "realFilename",
                path, 0L, "ext", "contentType", null, storagesId,
                cytomine.getCurrentUser().getId(), 0L, null).save();


        assertEquals(path, uf.get("path"), "Not the path used for the file creation");

        uf = new UploadedFile().fetch(uf.getId());

        uf.set("path", path+"bis");
        uf.update();
        assertEquals(path+"bis", uf.get("path"), "Not the path used for the project update");

        uf.delete();
        /*try {
            new UploadedFile().fetch(uf.getId());
            assert false;
        } catch (CytomineException e) { }
        */
//    }

    /*@Test
    void testAbstractImages() throws CytomineException {
        log.info("test abstract images methods");
        String name = UUID.randomUUID().toString();

        AbstractImage ai = new AbstractImage(name, "tiff").save();

        assertEquals(name, ai.get("filename"), "Not the name used for the image creation");

        ai = new AbstractImage().fetch(ai.getId());

        ai.set("filename", name+"bis");
        ai.update();
        assertEquals(name+"bis", ai.get("filename"), "Not the name used for the image update");

        ai.delete();
        try {
            new AbstractImage().fetch(ai.getId());
            assert false;
        } catch (CytomineException e) { }

    }*/

    @Test
    void testAbstractImages() throws CytomineException {
        log.info("test image instances methods");
        String name = UUID.randomUUID().toString();

        AbstractImage ai = new AbstractImage(name, "tiff").save();
        ImageInstance ii = new ImageInstance(ai.getId(), project.getId()).save();

        //assertEquals(name, ai.get("filename"), "Not the name used for the image creation");

        ii = new ImageInstance().fetch(ii.getId());

        /*ai.set("filename", name+"bis");
        ai.update();
        assertEquals(name+"bis", ai.get("filename"), "Not the name used for the image update");
*/
        ii.delete();
        try {
            new ImageInstance().fetch(ii.getId());
            assert false;
        } catch (CytomineException e) { }
    }

    @Test
    void testAnnotations() throws CytomineException {
        log.info("test annotation methods");
        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        ArrayList<Long> termsId = new ArrayList<>(); termsId.add(term.getId());
        Annotation annot = new Annotation(polygon, image.getId(), termsId).save();

        annot = new Annotation().fetch(annot.getId());
        assertEquals(polygon, annot.get("location"), "fetched polygon not the same used for the annotation creation");

        String polygon2 = "POLYGON ((0 0, 0 20, 20 30, 20 0, 0 0))";
        annot.set("location", polygon2);
        annot.update();
        assertEquals(polygon2, annot.get("location"), "Not the polygon used for the annotation update");

        annot.delete();
        try {
            new Annotation().fetch(annot.getId());
            assert false;
        } catch (CytomineException e) { }
    }

    @Test
    void testProperties() throws CytomineException {
        log.info("test property methods");
        String key = "test";

        Property propertyImg = new Property(ImageInstance.class.getSimpleName(), image.getId(), key, "foo").save();

        /*
        annot = new Annotation().fetch(annot.getId());
        assertEquals(polygon, annot.get("location"), "fetched polygon not the same used for the annotation creation");

        String polygon2 = "POLYGON ((0 0, 0 20, 20 30, 20 0, 0 0))";
        annot.set("location", polygon2);
        annot.update();
        assertEquals(polygon2, annot.get("location"), "Not the polygon used for the annotation update");

        annot.delete();
        try {
            new Annotation().fetch(annot.getId());
            assert false;
        } catch (CytomineException e) { }
        */
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }

}