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
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.UserCollection;
import be.cytomine.client.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AnnotationTests {

    private static final Logger log = LogManager.getLogger(AnnotationTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }


    @Test
    void testCreateAnnotation() throws CytomineException {
        log.info("test create annotation");
        ImageInstance image = Utils.getImageInstance();
        Annotation a = new Annotation("POLYGON ((1983 2168, 2107 2160, 2047 2074, 1983 2168))", image).save();
        assertEquals(image.getId(), a.get("image"), "image not the same used for the annotation creation");

        a = new Annotation().fetch(a.getId());
        assertEquals(image.getId(), a.get("image"), "fetched image not the same used for the annotation creation");

        String locationBis = "POLYGON ((1983 2169, 2107 2160, 2047 2074, 1983 2169))";
        assertNotEquals(locationBis, a.get("location"), "Not the location used for the annotation update");
        a.set("location", locationBis);
        a.update();
        assertEquals(locationBis, a.get("location"), "Not the location used for the annotation update");

        a.delete();
        try {
            new Annotation().fetch(a.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateAnnotationWithTerms() throws CytomineException {
        log.info("test create annotation with terms");
        ImageInstance image = Utils.getImageInstance();
        Term term1 = Utils.getTerm();
        Term term2 = Utils.getNewTerm();
        List<Long> idTerms = new ArrayList<>();
        idTerms.add(term1.getId());
        idTerms.add(term2.getId());
        Annotation a = new Annotation("POLYGON ((1983 2168, 2107 2160, 2047 2074, 1983 2168))", image.getId(), idTerms).save();
        assertEquals(idTerms.toString().replace(" ",""), a.getStr("term"), "terms not the same used for the annotation creation");
    }

    @Test
    void testCreateAnnotationIncorrect() throws CytomineException {
        log.info("test create incorrect annotation");

        try {
            new Annotation().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testListAllAnnotations() throws CytomineException {
        log.info("test list all annotations");
        try {
            Collection<Annotation> c = Collection.fetch(Annotation.class);
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 400);
        }
        try {
            AnnotationCollection ac = new AnnotationCollection(0,0);
            ac.fetch();
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 400);
        }
    }

    @Test
    void testListAnnotationsByProject() throws CytomineException {
        log.info("test list annotations in a project");
        Project project = Utils.getProject();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("project", project.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        Utils.getNewAnnotation();
        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        ac = AnnotationCollection.fetchByProject(project);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testListAnnotationsByUser() throws CytomineException {
        log.info("test list annotations by an user");
        User user = User.getCurrent();
        Project project = Utils.getProject();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("user", user.getId().toString());
        ac.addFilter("project", project.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        Utils.getNewAnnotation();
        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        ac = AnnotationCollection.fetchByUserAndProject(user, project);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testListAnnotationsByOntology() throws CytomineException {
        log.info("test list annotations where term are into an ontology");
        Ontology ontology = Utils.getOntology();
        Project project = Utils.getProject();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("ontology", ontology.getId().toString());
        ac.addFilter("project", project.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        Utils.getNewAnnotation();
        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        Map<String, Object> params = new HashMap<>();
        params.put("ontology", ontology.getId());
        params.put("project", project.getId());
        ac = AnnotationCollection.fetchWithParameters(params);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testListAnnotationsByImage() throws CytomineException {
        log.info("test list annotations in a image");
        ImageInstance image = Utils.getImageInstance();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("image", image.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        Utils.getNewAnnotation();
        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        ac = AnnotationCollection.fetchByImageInstance(image);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testListAnnotationsByTermAndProject() throws CytomineException {
        log.info("test list annotations where term are into an ontology");
        Project project = Utils.getProject();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("term", Utils.getTerm().getId().toString());
        ac.addFilter("project", project.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        ImageInstance image = Utils.getImageInstance();
        Term term = Utils.getTerm();
        List<Long> idTerms = new ArrayList<>();
        idTerms.add(term.getId());
        Annotation a = new Annotation("POLYGON ((1983 2168, 2107 2160, 2047 2074, 1983 2168))", image.getId(), idTerms).save();

        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        ac = AnnotationCollection.fetchByTermAndProject(term, project);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testListAnnotationsByTermAndImage() throws CytomineException {
        log.info("test list annotations where term are into an ontology");
        ImageInstance image = Utils.getImageInstance();
        AnnotationCollection ac = new AnnotationCollection(0,0);
        ac.addFilter("term", Utils.getTerm().getId().toString());
        ac.addFilter("image", image.getId().toString());
        ac.fetch();

        int size = ac.size();
        log.info(ac.size());

        Term term = Utils.getTerm();
        List<Long> idTerms = new ArrayList<>();
        idTerms.add(term.getId());
        Annotation a = new Annotation("POLYGON ((1983 2168, 2107 2160, 2047 2074, 1983 2168))", image.getId(), idTerms).save();

        ac.fetch();

        assertEquals(size+1, ac.size());
        log.info(ac.size());

        size = ac.size();
        ac = AnnotationCollection.fetchByTermAndImageInstance(term, image);
        assertEquals(size, ac.size());
        log.info(ac.size());
    }

    @Test
    void testSimplify() throws CytomineException {
        log.info("test simplify an annotation");
        Annotation a = Utils.getAnnotation();
        a.simplify(0L,100L);

    }

}
