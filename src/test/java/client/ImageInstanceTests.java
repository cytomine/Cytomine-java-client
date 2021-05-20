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
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.AbstractImage;
import be.cytomine.client.models.ImageInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageInstanceTests {

    private static final Logger log = LogManager.getLogger(ImageInstanceTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateImageInstance() throws CytomineException {
        log.info("test create image_instance");
        String name = Utils.getRandomString();
        AbstractImage ai = new AbstractImage(name, "image/tiff").save();
        ImageInstance ii = new ImageInstance(ai, Utils.getProject()).save();
        assertEquals(name, ii.getStr("instanceFilename"), "instanceFilename not the same used for the abstract_image creation");

        ii = new ImageInstance().fetch(ii.getId());
        assertEquals(name, ii.getStr("instanceFilename"), "fetched instanceFilename not the same used for the abstract_image creation");

        ii.set("instanceFilename", name+"bis");
        ii.update();
        assertEquals(name+"bis", ii.getStr("instanceFilename"), "instanceFilename not the same used for the image_instance update");

        ii.delete();
        try {
            new ImageInstance().fetch(ii.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateImageInstanceIncorrect() throws CytomineException {
        log.info("test create incorrect image_instance");

        try {
            new ImageInstance().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testListImageInstances() throws CytomineException {
        log.info("test list image_instances");
        try {
            Collection<ImageInstance> c = Collection.fetch(ImageInstance.class);
            assert false;
        } catch (CytomineException e) {
            assertEquals(404, e.getHttpCode());
        }
    }

    @Test
    void testListImageInstancesOfAProject() throws CytomineException {
        log.info("test list image_instances in a project");
        Collection<ImageInstance> c = new Collection<>(ImageInstance.class, 0,0);
        c.addFilter("project", Utils.getProject().getId().toString());
        c.fetch();

        int size = c.size();
        log.info(c.size());

        c = ImageInstanceCollection.fetchByProject(Utils.getProject());
        assertEquals(size,c.size());
        log.info(c.size());
    }

}
