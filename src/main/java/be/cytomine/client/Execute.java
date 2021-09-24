package be.cytomine.client;

/*
 * Copyright (c) 2009-2018. Authors: see NOTICE file.
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

import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.collections.TermCollection;
import be.cytomine.client.models.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Execute {

    private static final Logger log = Logger.getLogger(Execute.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
//        PropertyConfigurator.configure("log4j.properties");
        log.info("Connection to cytomine...");

        Cytomine.connection(args[0], args[1], args[2]);


        Project project = new Project().fetch(526102245L);
        Ontology ontology = new Ontology().fetch(project.getLong("ontology"));
        TermCollection terms = TermCollection.fetchByOntology(ontology);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("project", 526102245L);
        parameters.put("showMeta", true);
        parameters.put("showTerm", true);
        parameters.put("showWKT", true);
        AnnotationCollection ac = AnnotationCollection.fetchWithParameters(parameters);
        for (int i = 0; i < ac.size(); i++) {
            Annotation annot = ac.get(i);
            System.out.println("Annnotation id " + annot.getStr("id") + " has terms: ");
            List annotTermsIds = annot.getList("term");
            for (int j = 0; j < annotTermsIds.size(); j++) {
                for (int k = 0; k < terms.size(); k++) {
                    Term currentTerm = terms.get(k);
                    if (currentTerm.getId().equals((Long) annotTermsIds.get(j))) {
                        System.out.println(currentTerm.getStr("name"));
                    }
                }
            }
        }


//        ImageInstance img = new ImageInstance().fetch(526163790L);
//        AnnotationCollection ac = AnnotationCollection.fetchByImageInstance(img);
////        AnnotationCollection ac = AnnotationCollection.fetchWithParameters(parameters, 0, 1);
//        System.out.println(ac.size());
////        Project p = new Project().fetch(39738769L);
////        ImageInstanceCollection ic = ImageInstanceCollection.fetchByProject(p);
////        System.out.println(ic.get(0));

//        ping();
    }

    public static void ping() throws CytomineException {
        log.info("Hello " + Cytomine.getInstance().getCurrentUser().get("username"));
    }
    public static void ping(Cytomine cytomine) throws CytomineException {
        log.info("Hello " + cytomine.getCurrentUser().get("username"));
    }
}
