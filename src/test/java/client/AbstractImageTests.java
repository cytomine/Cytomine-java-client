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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.AbstractImage;
import be.cytomine.client.models.UploadedFile;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractImageTests {

    private static final Logger log = Logger.getLogger(AbstractImageTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateAbstractImage() throws CytomineException {
        log.info("test create abstract_image");
        String name = Utils.getRandomString();
        AbstractImage ai = new AbstractImage(name, "image/tiff").save();
        assertEquals("image/tiff", ai.getStr("mime"), "mime not the same used for the abstract_image creation");

        ai = new AbstractImage().fetch(ai.getId());
        assertEquals("image/tiff", ai.getStr("mime"), "fetched mime not the same used for the abstract_image creation");

        /* TODO permissions
        ai.set("width", 1000);
        ai.update();
        assertEquals(1000, (int)ai.getInt("width"), "Not the same width used for the abstract_image update");

        ai.delete();
        try {
            new AbstractImage().fetch(ai.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }*/
    }

    @Test
    void testCreateAbstractImageIncorrect() throws CytomineException {
        log.info("test create incorrect abstract_image");

        try {
            new AbstractImage().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testListAbstractImages() throws CytomineException {
        log.info("test list abstract_images");
        Collection<AbstractImage> c = Collection.fetch(AbstractImage.class);

        log.info(c.size());
    }

}
