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

import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.AbstractSlice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractSliceTests {

    private static final Logger log = LogManager.getLogger(AbstractSliceTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateAbstractSlice() throws CytomineException {
        log.info("test create abstract_image");
        String name = Utils.getRandomString();
        AbstractSlice as = new AbstractSlice(Utils.getNewAbstractImage(), Utils.getNewUploadedFile(), "image/pyrtiff", 0, 0, 0).save();
        assertEquals("image/pyrtiff", as.getStr("mime"), "mime not the same used for the abstract_slice creation");

        as = new AbstractSlice().fetch(as.getId());
        assertEquals("image/pyrtiff", as.getStr("mime"), "fetched mime not the same used for the abstract_slice creation");

        /* TODO permissions
        ai.set("width", 1000);
        ai.update();
        assertEquals(1000, (int)ai.getInt("width"), "Not the same width used for the abstract_image update");

        ai.delete();
        try {
            new AbstractSlice().fetch(ai.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }*/
    }

    @Test
    void testCreateAbstractSliceIncorrect() throws CytomineException {
        log.info("test create incorrect abstract_slice");

        try {
            new AbstractSlice().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testListAbstractSlices() throws CytomineException {
        log.info("test list abstract_slices");
        Collection<AbstractSlice> c = new Collection<>(AbstractSlice.class, 0, 0);
        c.addFilter("abstractimage", Utils.getAbstractImage().getId().toString());
        c.fetch();

        log.info(c.size());
    }

}
