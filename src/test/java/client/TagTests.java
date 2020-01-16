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
import be.cytomine.client.models.Tag;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTests {

    private static final Logger log = Logger.getLogger(TagTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateTag() throws CytomineException {
        log.info("test create tag");
        String name = Utils.getRandomString();
        Tag t = new Tag(name).save();
        assertEquals(name, t.get("name"), "name not the same used for the tag creation");

        t = new Tag().fetch(t.getId());
        assertEquals(name, t.get("name"), "fetched name not the same used for the tag creation");

        t.set("name", name+"bis");
        t.update();
        assertEquals(name+"bis", t.get("name"), "Not the name used for the tag update");

        t.delete();
        try {
            new Tag().fetch(t.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateTagIncorrect() throws CytomineException {
        log.info("test create incorrect tag");

        try {
            new Tag().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }
    @Test
    void testListTags() throws CytomineException {
        log.info("test list tags");
        Collection<Tag> c = Collection.fetch(Tag.class);

        log.info(c.size());
    }
}
