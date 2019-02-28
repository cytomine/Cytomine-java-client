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
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermTests {

    private static final Logger log = Logger.getLogger(TermTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateTerm() throws CytomineException {
        log.info("test create term");
        String name = Utils.getRandomString();
        Term t = new Term(name,"#FF0000", Utils.getOntology()).save();
        assertEquals(name, t.get("name"), "name not the same used for the term creation");

        t = new Term().fetch(t.getId());
        assertEquals(name, t.get("name"), "fetched name not the same used for the term creation");

        t.set("name", name+"bis");
        t.update();
        assertEquals(name+"bis", t.get("name"), "Not the name used for the term update");

        t.delete();
        try {
            new Term().fetch(t.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateTermIncorrect() throws CytomineException {
        log.info("test create incorrect term");

        try {
            new Term().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }

        String name = Utils.getRandomString();
        Ontology ontology = Utils.getOntology();
        try {
            Term t = new Term();
            t.set("name", name);
            t.set("ontology", ontology.getId());
            t.save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }


    @Test
    void testListTerms() throws CytomineException {
        log.info("test list terms");
        Collection<Term> c = Collection.fetch(Term.class);

        log.info(c.size());
    }

    @Test
    void testListTermsByOntology() throws CytomineException {
        log.info("test list terms");
        Collection<Term> c = new Collection<>(Term.class,0,0);
        c.addFilter("ontology", Utils.getOntology().getId().toString());
        c.fetch();
        log.info(c.size());
    }

}
