package be.cytomine.client.collections;

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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Ontology;
import be.cytomine.client.models.Term;

public class TermCollection extends Collection<Term> {

    public TermCollection(int offset, int max) {
        super(Term.class, max, offset);
    }

    public static TermCollection fetchByOntology(Ontology ontology) throws CytomineException {
        return fetchByOntology(Cytomine.getInstance().getDefaultCytomineConnection(), ontology, 0,0);
    }
    public static TermCollection fetchByOntology(CytomineConnection connection, Ontology ontology) throws CytomineException {
        return fetchByOntology(connection, ontology, 0,0);
    }

    public static TermCollection fetchByOntology(Ontology ontology, int offset, int max) throws CytomineException {
        return fetchByOntology(Cytomine.getInstance().getDefaultCytomineConnection(), ontology, offset,max);
    }

    public static TermCollection fetchByOntology(CytomineConnection connection, Ontology ontology, int offset, int max) throws CytomineException {
        return (TermCollection) new TermCollection(max, offset).fetchWithFilter(connection, Ontology.class, ontology.getId(), offset, max);
    }


}
