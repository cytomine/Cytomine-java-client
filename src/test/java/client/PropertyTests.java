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

import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.PropertyCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.Property;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyTests {

    private static final Logger log = Logger.getLogger(PropertyTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateProperty() throws CytomineException {
        log.info("test create property");
        Project project = Utils.getProject();
        String value = Utils.getRandomString();

        Property p = new Property(project, "key", value).save();
        assertEquals(value, p.get("value"), "fetched value not the same used for the property creation");

        p = new Property(project).fetch("key");
        assertEquals(value, p.get("value"), "fetched value not the same used for the property creation");

        p.set("value", value+"bis");
        p.update();
        assertEquals(value+"bis", p.get("value"), "Not the value used for the property update");

        p.delete();
        try {
            new Property(project).fetch("key");
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }

    }

    @Test
    void testCreatePropertyIncorrect() throws CytomineException {
        log.info("test create incorrect property");
        Project project = Utils.getProject();

        try{
            new Property().save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 404);
        }
        try{
            new Property(project).save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 400);
        }
    }

    @Test
    void testListProperty() throws CytomineException {
        log.info("test list property");
        PropertyCollection pc = new PropertyCollection();
        try {
            pc.fetch();
            assert false;
        } catch(CytomineException e){
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testListProjectProperty() throws CytomineException {
        log.info("test list properties in a project");
        Project project = Utils.getProject();

        PropertyCollection pc = new PropertyCollection();
        pc.addFilter("project", project.getId().toString());
        pc.fetch();

        int size = pc.size();
        log.info(pc.size());

        pc = PropertyCollection.fetchByAssociatedDomain(project);
        assertEquals(size, pc.size());
        log.info(pc.size());
    }

    @Test
    void testListImageInstanceProperty() throws CytomineException {
        log.info("test list properties in an image");
        ImageInstance image = Utils.getImageInstance();

        PropertyCollection pc = new PropertyCollection();
        pc.addFilter("imageinstance", image.getId().toString());
        pc.fetch();

        int size = pc.size();
        log.info(pc.size());

        pc = PropertyCollection.fetchByAssociatedDomain(image);
        assertEquals(size, pc.size());
        log.info(pc.size());
    }

    @Test
    void testListAnnotationProperty() throws CytomineException {
        log.info("test list properties of an annotation");
        Annotation annotation = Utils.getAnnotation();

        PropertyCollection pc = new PropertyCollection();
        pc.addFilter("annotation", annotation.getId().toString());
        pc.fetch();

        int size = pc.size();
        log.info(pc.size());

        pc = PropertyCollection.fetchByAssociatedDomain(annotation);
        assertEquals(size, pc.size());
        log.info(pc.size());
    }

    /*@Test
    void testListOtherDomainProperty() throws CytomineException {
        assert false;
    }*/


}
