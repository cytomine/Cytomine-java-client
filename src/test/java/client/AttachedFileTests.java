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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AttachedFileCollection;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.AttachedFile;
import be.cytomine.client.models.Project;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttachedFileTests {

    private static final Logger log = Logger.getLogger(AttachedFileTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateAttachedFile() throws CytomineException {
        log.info("test create attached file");
        Project project = Utils.getProject();
        AttachedFile af = new AttachedFile(project, Utils.getFile()).save();
        assertEquals(Utils.getFile().getName(), af.get("filename"), "filename not the same used for the attached file creation");

        af = new AttachedFile().fetch(af.getId());
        assertEquals(Utils.getFile().getName(), af.get("filename"), "fetched filename not the same used for the attached file creation");

        af.delete();
        try {
            new AttachedFile().fetch(af.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(404, e.getHttpCode());
        }
    }

    @Test
    void testCreateAttachedFileIncorrect() throws CytomineException {
        log.info("test create incorrect attached file");

        try{
            new AttachedFile().save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 400);
        }
    }

    @Test
    void testCreateAttachedFileWithOtherName() throws CytomineException {
        log.info("test create attached file with another filename");
        Project project = Utils.getProject();
        AttachedFile af = new AttachedFile(project, Utils.getFile());
        af.set("filename", "newTest");
        af = af.save();
        assertEquals("newTest", af.get("filename"), "filename not the same used for the attached file creation");
    }

    @Test
    void testListAttachedFile() throws CytomineException {
        log.info("test list attached files");
        Collection<AttachedFile> c = Collection.fetch(AttachedFile.class);

        log.info(c.size());
    }

    @Test
    void testListAttachedFilesOfADomain() throws CytomineException {
        log.info("test list attached files into a project");
        Project project = Utils.getProject();
        AttachedFileCollection c = new AttachedFileCollection();
        c.addFilter("domainClassName", Cytomine.convertDomainName(project.getClass().getSimpleName().toLowerCase()));
        c.addFilter("domainIdent", project.getId().toString());
        c.fetch();

        log.info(c.size());

        AttachedFileCollection atc = new AttachedFileCollection(project);
        atc.fetch();
        int size = c.size();
        log.info(c.size());

        c = AttachedFileCollection.fetchByAssociatedDomain(Utils.getProject());
        assertEquals(size,c.size());
        log.info(c.size());
    }
}
