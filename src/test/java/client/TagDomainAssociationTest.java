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
import be.cytomine.client.collections.TagDomainAssociationCollection;
import be.cytomine.client.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagDomainAssociationTest {

    private static final Logger log = LogManager.getLogger(TagDomainAssociationTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateTagDomainAssociation() throws CytomineException {
        log.info("test create tag domain association");
        Project project = Utils.getProject();
        Tag tag = Utils.getTag();

        TagDomainAssociation tda = new TagDomainAssociation(project, tag).save();
        assertEquals(tag.getId(), tda.get("tag"), "fetched value not the same used for the tag domain association creation");

        tda = new TagDomainAssociation(project).fetch(tda.getId());
        assertEquals(tag.getId(), tda.get("tag"), "fetched value not the same used for the tag domain association creation");

        tda.delete();
        try {
            new TagDomainAssociation(project).fetch(tda.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateTagDomainAssociationIncorrect() throws CytomineException {
        log.info("test create incorrect tag domain association");
        Project project = Utils.getProject();

        try{
            new TagDomainAssociation().save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 404);
        }
        try{
            new TagDomainAssociation(project).save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 400);
        }
    }

    @Test
    void testListTagDomainAssociation() throws CytomineException {
        log.info("test list tag domain association");
        Collection<TagDomainAssociation> c = new Collection<>(TagDomainAssociation.class,0,0);
        try {
            c.fetch();
            assert false;
        } catch(CytomineException e){
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testListProjectTagDomainAssociation() throws CytomineException {
        log.info("test list tag associations in a project");
        Project project = Utils.getNewProject();

        TagDomainAssociationCollection c = new TagDomainAssociationCollection();
        c.addFilter("project", project.getId().toString());
        c.fetch();

        int size = c.size();
        log.info(c.size());


        Tag tag = Utils.getTag();
        new TagDomainAssociation(project, tag).save();

        c = TagDomainAssociationCollection.fetchByAssociatedDomain(project);
        assertEquals(size+1, c.size());
        log.info(c.size());
    }

    @Test
    void testListImageInstanceTagDomainAssociation() throws CytomineException {
        log.info("test list tag associations in an image");
        ImageInstance image = Utils.getNewImageInstance();

        TagDomainAssociationCollection c = new TagDomainAssociationCollection();
        c.addFilter("imageinstance", image.getId().toString());
        c.fetch();

        int size = c.size();
        log.info(c.size());


        Tag tag = Utils.getTag();
        new TagDomainAssociation(image, tag).save();

        c = TagDomainAssociationCollection.fetchByAssociatedDomain(image);
        assertEquals(size+1, c.size());
        log.info(c.size());
    }
}
