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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.StorageCollection;
import be.cytomine.client.models.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModelsTest {

    private static final Logger log = Logger.getLogger(ModelsTest.class);
    private static String host;
    private static String publicKey;
    private static String privateKey;
    private static Cytomine cytomine;

    private static Ontology ontology;
    private static Project project;
    private static ImageInstance image;
    private static Term term;

    private static Software software;
    private static Job job;
    private static Annotation annotation;

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

        log.info("Hello " + cytomine.getCurrentUser().get("username"));

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

        software = new Software(UUID.randomUUID().toString(),"createRabbitJobService","","").save();
        job = new Job();
        job.set("software", software.getId());
        job.set("project", project.getId());
        job.save();


        String name = UUID.randomUUID().toString();
        AbstractImage ai = new AbstractImage(name, "image/tiff").save();
        image = new ImageInstance(ai.getId(), project.getId()).save();

        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        annotation = new Annotation(polygon, image.getId()).save();
    }

    @BeforeEach
    void init() {
    }

    private void saveModelTest(Model m, String property) throws IllegalAccessException, InstantiationException, CytomineException {
        log.info("test save");
        m.save();

        assertNotNull(m.getId());

        log.info("test fetch");
        Model m2 = m.getClass().newInstance().fetch(m.getId());
        assertNotNull(m2);
        String propertyValue = m2.getStr(property);
        assertEquals(m.getStr(property), propertyValue, "fetched "+property+" not the same used for the "+m.getClass().getSimpleName()+" creation");
    }
    private void updateModelTest(Model m, String property) throws IllegalAccessException, InstantiationException, CytomineException {
        String propertyValue = m.getStr(property);
        m.set(property, propertyValue+"1");
        log.info("test update");
        m.update();

        try {
            Long value = Long.parseLong(propertyValue);
            assertEquals(value+1, (long) m.getLong(property), "Not the "+property+" used for the "+m.getClass().getSimpleName()+" update");
        } catch(NumberFormatException e){
            assertEquals(propertyValue+"1", m.getStr(property), "Not the "+property+" used for the "+m.getClass().getSimpleName()+" update");
        }
    }
    private void deleteModelTest(Model m) throws IllegalAccessException, InstantiationException, CytomineException {
        log.info("test delete");
        m.delete();
        try {
            m = m.getClass().newInstance().fetch(m.getId());
            assertNotNull(m.get("deleted"));
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(),404);
        }
    }
    private void commonModelTests(Model m, String property) throws IllegalAccessException, InstantiationException, CytomineException {
        saveModelTest(m,property);
        updateModelTest(m,property);
        deleteModelTest(m);
    }
/*
    @Test
    void testOntology() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test ontology methods");
        String name = UUID.randomUUID().toString();
        Ontology o = new Ontology(name);

        commonModelTests(o,"name");
    }
    @Test
    void testTerm() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test term methods");
        String name = UUID.randomUUID().toString();
        Term t = new Term(name,"#000000",ontology.getId());

        commonModelTests(t,"name");
    }
    @Test
    void testProject() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test project methods");
        String name = UUID.randomUUID().toString();
        Project p = new Project(name,ontology.getId());

        commonModelTests(p,"name");
    }

    @Test
    void testSoftware() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test software methods");
        String name = UUID.randomUUID().toString();
        Software s = new Software(name,"createRabbitJobService","","");

        commonModelTests(s,"name");
    }
    @Test
    void testSoftwareParameter() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test software parameter methods");
        String name = UUID.randomUUID().toString();
        SoftwareParameter s = new SoftwareParameter(name, "", software.getId(), "", true, 0);

        commonModelTests(s,"name");
    }
    @Test
    void testSoftwareProject() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test software project methods");
        SoftwareProject s = new SoftwareProject(software.getId(),project.getId());

        assert false;
        commonModelTests(s,"name");
    }

    @Test
    void testJob() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test job methods");
        Job j = new Job();
        j.set("software", software.getId());
        j.set("project", project.getId());

        commonModelTests(j,"status");
    }
    @Test
    void testUserJob() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test user job methods");
        //Job j = new Job();
        //j.save();

        String name = UUID.randomUUID().toString();
        UserJob uj = new UserJob();
        uj.set("username",name);
        uj.set("password", "PasswordUserJob");
        //uj.set("job", j.getId());
        uj.set("software", software.getId());
        uj.set("project", project.getId());

        saveModelTest(uj,"password");
    }
    @Test
    void testJobParameter() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test job parameter methods");
        String name = UUID.randomUUID().toString();
        SoftwareParameter sp = new SoftwareParameter(name, "", software.getId(), "", true, 0).save();
        JobParameter jp = new JobParameter(job.getId(), sp.getId(), "");

        commonModelTests(jp,"value");
    }
    @Test
    void testJobData() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test job data methods");
        JobData jd = new JobData("test", job.getId(),"test.txt");

        commonModelTests(jd,"filename");
    }
    @Test
    void testJobTemplate() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test job template methods");
        String name = UUID.randomUUID().toString();
        JobTemplate jt = new JobTemplate(name, project.getId(), software.getId());

        commonModelTests(jt,"name");
    }

    @Test
    void testUploadedFile() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test uploaded file methods");
        String name = UUID.randomUUID().toString();

        Collection<Storage> storages = Collection.fetch(Storage.class, 0, 1).fetch();
        ArrayList<Long> storagesId = new ArrayList<>();
        storagesId.add(storages.get(0).getId());


        UploadedFile uf = new UploadedFile(name, name, "/", 0L, ".txt", "", null, storagesId, Cytomine.getInstance().getCurrentUser().getId(), UploadedFile.Status.UPLOADED, null);

        commonModelTests(uf,"originalFilename");
    }
    @Test
    void testAbstractImage() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test abstract image methods");
        String name = UUID.randomUUID().toString();
        AbstractImage ai = new AbstractImage(name, "image/tiff");

        log.info("test save");
        ai.save();

        assertNotNull(ai.getId());

        log.info("test fetch");
        AbstractImage ai2 = new AbstractImage().fetch(ai.getId());
        assertNotNull(ai2);
        String propertyValue = ai2.getStr("filename");
        assertEquals(ai.getStr("filename"), propertyValue, "fetched filename not the same used for the "+ai.getClass().getSimpleName()+" creation");


        Collection<Storage> storages = Collection.fetch(Storage.class, 0, 1).fetch();
        new StorageAbstractImage(storages.get(0).getId(), ai.getId()).save();


        updateModelTest(ai,"filename");
        deleteModelTest(ai);
    }
    @Test
    void testImageInstance() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test image instance methods");
        String name = UUID.randomUUID().toString();
        AbstractImage ai = new AbstractImage(name, "image/tiff").save();
        ImageInstance ii = new ImageInstance(ai.getId(), project.getId());

        commonModelTests(ii,"instanceFilename");
    }

    @Test
    void testAnnotation() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test annotation methods");
        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        Annotation a = new Annotation(polygon, image.getId());

        commonModelTests(a,"geometryCompression");
    }
    @Test
    void testReviewedAnnotation() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test reviewed annotation methods");
        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        ReviewedAnnotation ra = new ReviewedAnnotation(polygon, image, project, terms, Cytomine.getInstance().getCurrentUser().getId(), Cytomine.getInstance().getCurrentUser().getId(), annotation);


        commonModelTests(ra,"status");
    }

    @Test
    void testUser() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test user methods");
        String name = UUID.randomUUID().toString();
        User u = new User(name, "first", "last", "test@test.be", "password");

        commonModelTests(u,"firstname");
    }
    */
    /*@Test
    void testAttachedFile() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test attached file methods");
        AttachedFile a = new AttachedFile(annotation, "");

        commonModelTests(a,"password");
    }*/
    @Test
    void testDescription() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test description methods");
        Description d = new Description(annotation, "test");

        d.save();

        assertNotNull(d.getId());

        log.info("test fetch");
        Description d2 = d.getClass().newInstance().fetch(d.getStr("domainClassName"), d.getStr("domainIdent"));
        assertNotNull(d2);
        String propertyValue = d2.getStr("text");
        assertEquals(d.getStr("text"), propertyValue, "fetched text not the same used for the Description creation");

        updateModelTest(d,"text");
        deleteModelTest(d);
    }
    /*@Test
    void testProperty() throws CytomineException, InstantiationException, IllegalAccessException {
        log.info("test reviewed annotation methods");
        String polygon = "POLYGON ((0 0, 0 20, 20 20, 20 0, 0 0))";
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        Property ra = new Property(annotation, "n");

        commonModelTests(ra,"password");
    }



    /**



     AmqpQueue

     Storage
     StorageAbstractImage
     ImageGroup
     ImageSequence


     AnnotationUnion
     AnnotationTerm


     AttachedFile
     Description
     Property

     ProcessingServer
     ImageFilter
     User
     Role
     DeleteCommand
     */


    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }

}