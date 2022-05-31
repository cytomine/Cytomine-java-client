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
import be.cytomine.client.models.Project;
import be.cytomine.client.models.Software;
import be.cytomine.client.models.SoftwareProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoftwareTest {

    private static final Logger log = LogManager.getLogger(SoftwareTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateSoftware() throws CytomineException {
        log.info("test create software");
        String name = Utils.getRandomString();
        Software s = new Software(name , "", "", "").save();
        assertEquals(name, s.get("name"), "name not the same used for the software creation");

        s = new Software().fetch(s.getId());
        assertEquals(name, s.get("name"), "fetched name not the same used for the software creation");

        s.set("name", name+"bis");
        s.update();
        assertEquals(name+"bis", s.get("name"), "Not the name used for the software update");

        s.delete();
        try {
            new Software().fetch(s.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateSoftwareIncorrect() throws CytomineException {
        log.info("test create incorrect software");

        try {
            new Software().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }
    @Test
    void testListSoftwares() throws CytomineException {
        log.info("test list softwares");
        Collection<Software> c = Collection.fetch(Software.class);

        log.info(c.size());
    }

    @Test
    void testSoftwareProject() throws CytomineException {
        log.info("test software project");
        Project project = Utils.getProject();
        Software software = Utils.getSoftware();
        SoftwareProject sp = new SoftwareProject(software, project).save();
        assertEquals(project.getId(), sp.get("project"), "project not the same used for the software_project creation");

        sp = new SoftwareProject().fetch(sp.getId());

        sp.delete();
        try {
            new SoftwareProject().fetch(sp.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

}
