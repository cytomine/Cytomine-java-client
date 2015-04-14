package be.cytomine.client.sample;
/*
 * Copyright (c) 2009-2015. Authors: see NOTICE file.
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
import be.cytomine.client.models.JobParameter;
import be.cytomine.client.models.JobTemplate;
import be.cytomine.client.models.Software;
import org.apache.log4j.Logger;

public class SoftwareExample {

    private static final Logger log = Logger.getLogger(SoftwareExample.class);

    public static void addSoftwareTestArea(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("ComputeAnnotationStat", "computeAnnotationStatService", "DownloadFiles", "groovy -cp algo/computeAnnotationStats/Cytomine-Java-Client.jar:algo/computeAnnotationStats/jts-1.13.jar algo/computeAnnotationStats/computeAnnotationStats.groovy");

            /**
             * 0: type (=> cytomine) or standalone if execute with ide/java -jar  => STRING
             * 1: public key
             * 2: private key
             * 3: N value
             * 4: T value
             * 5: Working dir
             * 6: Cytomine Host
             * 7: Force download crop (even if already exist) => BOOLEAN
             * 8: storeName (KYOTOSINGLEFILE)
             * 9: index project (list: x,y,z)
             * 10: search project (only one)
             */
            cytomine.addSoftwareParameter("execType", "String", software.getId(), "cytomine", false, 100);
            cytomine.addSoftwareParameter("annotation", "Number", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("term", "Number", software.getId(), "", true, 300);
//               cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
//               cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
//               cytomine.addSoftwareParameter("N", "Number", software.getId(), "500", false, 400);
//               cytomine.addSoftwareParameter("T", "Number", software.getId(), "5", false, 500);
//               cytomine.addSoftwareParameter("workingDir", "String", software.getId(), "algo/retrievalSuggest/suggest/", true, 600);
//               cytomine.addSoftwareParameter("cytomineHost", "String", software.getId(), "$cytomineHost$", true, 700);
//               cytomine.addSoftwareParameter("forceDownloadCrop", "Boolean", software.getId(), "false", false, 800);
//               cytomine.addSoftwareParameter("storeName", "String", software.getId(), "KYOTOSINGLEFILE", false, 900);
//               cytomine.addSoftwareParameter("indexProject", "ListDomain", software.getId(), "$currentProject$", true, 1000, "/api/ontology/$currentOntology$/project.json", "name", "name");
//               cytomine.addSoftwareParameter("searchProject", "Domain", software.getId(), "$currentProject$", true, 1100, "/api/ontology/$currentOntology$/project.json", "name", "name");
//
//               cytomine.addSoftwareProject(software.getId(), 67l);
//               cytomine.addSoftwareProject(software.getId(), 716498l);
//               cytomine.addSoftwareProject(software.getId(), 75985l);

        } catch (CytomineException e) {
            log.error(e);
        }
    }


    public static void testAddJobTemplate(Cytomine cytomine) throws Exception {
        JobTemplate jobTemplate = cytomine.addJobTemplate("ComputeAnnotationStat", 57l, 900532l);
        JobParameter jobParameter1 = cytomine.addJobParameter(jobTemplate.getId(), 900556l, "20202");
        JobParameter jobParameter2 = cytomine.addJobParameter(jobTemplate.getId(), 900562l, "456");
    }


    public static void addSoftwareTest(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("SoftwareTest", "retrievalSuggestedTermJobService", "ValidateAnnotation", "");

            /**
             * 0: type (=> cytomine) or standalone if execute with ide/java -jar  => STRING
             * 1: public key
             * 2: private key
             * 3: N value
             * 4: T value
             * 5: Working dir
             * 6: Cytomine Host
             * 7: Force download crop (even if already exist) => BOOLEAN
             * 8: storeName (KYOTOSINGLEFILE)
             * 9: index project (list: x,y,z)
             * 10: search project (only one)
             */
            cytomine.addSoftwareParameter("execType", "String", software.getId(), "cytomine", false, 100);
//               cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
//               cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
//               cytomine.addSoftwareParameter("N", "Number", software.getId(), "500", false, 400);
//               cytomine.addSoftwareParameter("T", "Number", software.getId(), "5", false, 500);
//               cytomine.addSoftwareParameter("workingDir", "String", software.getId(), "algo/retrievalSuggest/suggest/", true, 600);
//               cytomine.addSoftwareParameter("cytomineHost", "String", software.getId(), "$cytomineHost$", true, 700);
//               cytomine.addSoftwareParameter("forceDownloadCrop", "Boolean", software.getId(), "false", false, 800);
//               cytomine.addSoftwareParameter("storeName", "String", software.getId(), "KYOTOSINGLEFILE", false, 900);
//               cytomine.addSoftwareParameter("indexProject", "ListDomain", software.getId(), "$currentProject$", true, 1000, "/api/ontology/$currentOntology$/project.json", "name", "name");
//               cytomine.addSoftwareParameter("searchProject", "Domain", software.getId(), "$currentProject$", true, 1100, "/api/ontology/$currentOntology$/project.json", "name", "name");
//
//               cytomine.addSoftwareProject(software.getId(), 67l);
//               cytomine.addSoftwareProject(software.getId(), 716498l);
//               cytomine.addSoftwareProject(software.getId(), 75985l);

        } catch (CytomineException e) {
            log.error(e);
        }
    }

    public static void addSoftwareProject(Cytomine cytomine) throws Exception {
        cytomine.addSoftwareProject(485324l, 75985l);
    }

    public static void addSoftwareRetrieval(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("Retrieval-Suggest-Term", "retrievalSuggestedTermJobService", "ValidateAnnotation", "");

            /**
             * 0: type (=> cytomine) or standalone if execute with ide/java -jar  => STRING
             * 1: public key
             * 2: private key
             * 3: N value
             * 4: T value
             * 5: Working dir
             * 6: Cytomine Host
             * 7: Force download crop (even if already exist) => BOOLEAN
             * 8: storeName (KYOTOSINGLEFILE)
             * 9: index project (list: x,y,z)
             * 10: search project (only one)
             */
            cytomine.addSoftwareParameter("execType", "String", software.getId(), "cytomine", false, 100);
            cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
            cytomine.addSoftwareParameter("N", "Number", software.getId(), "500", false, 400);
            cytomine.addSoftwareParameter("T", "Number", software.getId(), "5", false, 500);
            cytomine.addSoftwareParameter("workingDir", "String", software.getId(), "algo/retrievalSuggest/suggest/", true, 600);
            cytomine.addSoftwareParameter("cytomineHost", "String", software.getId(), "$cytomineHost$", true, 700);
            cytomine.addSoftwareParameter("forceDownloadCrop", "Boolean", software.getId(), "false", false, 800);
            cytomine.addSoftwareParameter("storeName", "String", software.getId(), "KYOTOSINGLEFILE", false, 900);
            cytomine.addSoftwareParameter("indexProject", "ListDomain", software.getId(), "$currentProject$", true, 1000, "/api/ontology/$currentOntology$/project.json", "name", "name");
            cytomine.addSoftwareParameter("searchProject", "Domain", software.getId(), "$currentProject$", true, 1100, "/api/ontology/$currentOntology$/project.json", "name", "name");

            cytomine.addSoftwareProject(software.getId(), 67l);
            cytomine.addSoftwareProject(software.getId(), 716498l);
            cytomine.addSoftwareProject(software.getId(), 75985l);

        } catch (CytomineException e) {
            log.error(e);
        }
    }

    public static void addSoftwareRetrievalEvolution(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("Retrieval-Evolution", "retrievalEvolutionJobService", "ValidateEvolution", "");

            /**
             * 0: type (=> cytomine) or standalone if execute with ide/java -jar  => STRING
             * 1: public key
             * 2: private key
             * 3: N value
             * 4: T value
             * 5: Working dir
             * 6: Cytomine Host
             * 7: Force download crop (even if already exist) => BOOLEAN
             * 8: storeName (KYOTOSINGLEFILE)
             * 9: index project (list: x,y,z)
             * 10: search project (only one)
             */
            cytomine.addSoftwareParameter("execType", "String", software.getId(), "cytomine", false, 100);
            cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
            cytomine.addSoftwareParameter("N", "Number", software.getId(), "500", false, 400);
            cytomine.addSoftwareParameter("T", "Number", software.getId(), "5", false, 500);
            cytomine.addSoftwareParameter("workingDir", "String", software.getId(), "algo/retrievalSuggest/suggest/", true, 600);
            cytomine.addSoftwareParameter("cytomineHost", "String", software.getId(), "$cytomineHost$", true, 700);
            cytomine.addSoftwareParameter("forceDownloadCrop", "Boolean", software.getId(), "false", false, 800);
            cytomine.addSoftwareParameter("storeName", "String", software.getId(), "KYOTOSINGLEFILE", false, 900);
            cytomine.addSoftwareParameter("indexProject", "ListDomain", software.getId(), "$currentProject$", true, 1000, "/api/ontology/$currentOntology$/project.json", "name", "name");
            cytomine.addSoftwareParameter("searchProject", "Domain", software.getId(), "$currentProject$", true, 1100, "/api/ontology/$currentOntology$/project.json", "name", "name");
            cytomine.addSoftwareParameter("dateStart", "Date", software.getId(), "$currentProjectCreationDate$", true, 1200);
            cytomine.addSoftwareParameter("dateStop", "Date", software.getId(), "$currentDate$", true, 1300);
            cytomine.addSoftwareParameter("dateIncr", "String", software.getId(), "MONTH+1", true, 1400);
            cytomine.addSoftwareProject(software.getId(), 67l);
            cytomine.addSoftwareProject(software.getId(), 716498l);
            cytomine.addSoftwareProject(software.getId(), 75985l);

        } catch (CytomineException e) {
            log.error(e);
        }
    }

    public static void addSoftwareComputeArea(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("ComputeTermArea", "computeTermAreaJobService", "DownloadFiles", "");
            cytomine.addSoftwareParameter("cytomineHost", "String", software.getId(), "$cytomineHost$", true, 100);
            cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
            cytomine.addSoftwareParameter("Terms", "ListDomain", software.getId(), "", true, 400, "/api/project/$currentProject$/term.json", "name", "name");
            cytomine.addSoftwareParameter("Images", "ListDomain", software.getId(), "", true, 500, "/api/project/$currentProject$/imageinstance.json", "filename", "filename");

        } catch (CytomineException e) {
            log.error(e);
        }
    }
}
