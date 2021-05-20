package client;

/*
 * Copyright (c) 2009-2020. Authors: see NOTICE file.
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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.Description;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DescriptionTests {

    private static final Logger log = LogManager.getLogger(DescriptionTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateDescription() throws CytomineException {
        log.info("test create description");
        Project project = Utils.getProject();
        String description = Utils.getRandomString();
        Description d = new Description(project, description).save();
        assertEquals(description, d.get("data"), "fetched data not the same used for the description creation");

        d = new Description().fetch(project);
        assertEquals(description, d.get("data"), "fetched data not the same used for the description creation");

        d.set("data", description+"bis");
        d.update();
        assertEquals(description+"bis", d.get("data"), "Not the data used for the description update");

        d.delete();
        try {
            new Description().fetch(project);
            assert false;
        } catch (CytomineException e) {
            assertEquals(404, e.getHttpCode());
        }
    }

    @Test
    void testCreateDescriptionIncorrect() throws CytomineException {
        log.info("test create incorrect description");

        try{
            new Description().save();
        } catch (CytomineException e){
            //TODO must be 404
            assertEquals(e.getHttpCode(), 500);
        }
    }

    @Test
    void testListDescription() throws CytomineException {
        log.info("test list description");
        Collection<Description> c = Collection.fetch(Description.class);

        log.info(c.size());
    }

    @Test
    void testGetDescriptionOfADomain() throws CytomineException {
        log.info("test get description by its associated domain");
        String description = Utils.getRandomString();
        Description d;

        //project
        Project project = Utils.getNewProject();
        new Description(project, description).save();
        d = new Description().fetch(project);
        assertEquals(description, d.get("data"), "fetched data not the same used for the description creation");

        //image
        ImageInstance image = Utils.getNewImageInstance();
        new Description(image, description).save();
        d = new Description().fetch(image);
        assertEquals(description, d.get("data"), "fetched data not the same used for the description creation");

        //annotation
        Annotation annotation = Utils.getNewAnnotation();
        new Description(annotation, description).save();
        d = new Description().fetch(annotation);
        assertEquals(description, d.get("data"), "fetched data not the same used for the description creation");
    }

}
