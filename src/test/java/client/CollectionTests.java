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
import be.cytomine.client.collections.OntologyCollection;
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionTests {

    private static final Logger log = Logger.getLogger(CollectionTests.class);

    @BeforeAll
    static void init() throws CytomineException {

    }

    @Test
    void testFixCollectionBugWhenBrowsingUsingCollectionsGet() throws CytomineException {
        // https://github.com/cytomine/Cytomine-java-client/issues/20
        OntologyCollection ontologyCollection = new OntologyCollection(0, 10);
        ontologyCollection.add(new Ontology("o1"));
        ontologyCollection.add(new Ontology("o2"));

        assertEquals(ontologyCollection.get(0).getStr("name"), "o1");
        assertEquals(ontologyCollection.get(1).getStr("name"), "o2");

        List<Ontology> ontologyList = new ArrayList<>();
        for(int i=0; i<ontologyCollection.size(); i++) {
            ontologyList.add(ontologyCollection.get(i));
        }

        assertEquals(ontologyList.get(0).getStr("name"), "o1");
        assertEquals(ontologyList.get(1).getStr("name"), "o2");

        // before the fix GITHUB_20: we get both o2 for ontologyList.get(0) and ontologyList.get(1)

    }
}
