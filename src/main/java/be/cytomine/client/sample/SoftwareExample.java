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

        } catch (CytomineException e) {
            log.error(e);
        }
    }


    public static void testAddJobTemplate(Cytomine cytomine) throws Exception {
        JobTemplate jobTemplate = cytomine.addJobTemplate("ComputeAnnotationStat", 57L, 900532L);
        JobParameter jobParameter1 = cytomine.addJobParameter(jobTemplate.getId(), 900556L, "20202");
        JobParameter jobParameter2 = cytomine.addJobParameter(jobTemplate.getId(), 900562L, "456");
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

               cytomine.addSoftwareProject(software.getId(), 67L);
               cytomine.addSoftwareProject(software.getId(), 716498L);
               cytomine.addSoftwareProject(software.getId(), 75985L);

        } catch (CytomineException e) {
            log.error(e);
        }
    }

    public static void addSoftwareProject(Cytomine cytomine) throws Exception {
        cytomine.addSoftwareProject(485324L, 75985L);
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

            cytomine.addSoftwareProject(software.getId(), 67L);
            cytomine.addSoftwareProject(software.getId(), 716498L);
            cytomine.addSoftwareProject(software.getId(), 75985L);

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
            cytomine.addSoftwareProject(software.getId(), 67L);
            cytomine.addSoftwareProject(software.getId(), 716498L);
            cytomine.addSoftwareProject(software.getId(), 75985L);

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

    public static void addSoftwareComputeArea2(Cytomine cytomine) throws Exception {
        try {
            Software software = cytomine.addSoftware("ComputeTermArea2", "createRabbitJobService", "DownloadFiles", "");
            cytomine.addSoftwareParameter("host", "String", software.getId(), "$cytomineHost$", true, 100);
            cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
            cytomine.addSoftwareParameter("annotation", "Domain", software.getId(), "", true, 400);
            cytomine.addSoftwareParameter("term", "Domain", software.getId(), "", true, 500);

        } catch (CytomineException e) {
            log.error(e);
        }
    }

    public static void addSoftwareSegmentationModelBuilder(Cytomine cytomine) throws Exception {
        try{
            Software software = cytomine.addSoftware("3Pyxit_SegmentationModel_Builder", "createRabbitJobWithArgsService", "DownloadFiles", "python add_and_run_job.py --cytomine_host $cytomine_host --cytomine_public_key $cytomine_public_key --cytomine_private_key $cytomine_private_key --cytomine_base_path $cytomine_base_path --cytomine_id_software $cytomine_id_software --cytomine_working_path $cytomine_working_path --cytomine_id_project $cytomine_id_project --cytomine_annotation_projects $cytomine_annotation_projects -z $z --cytomine_predict_terms $cytomine_predict_terms --cytomine_excluded_terms $cytomine_excluded_terms --pyxit_target_width $pyxit_target_width --pyxit_target_height $pyxit_target_height --pyxit_colorspace $pyxit_colorspace --pyxit_n_jobs $pyxit_n_jobs --pyxit_save_to $pyxit_save_to --pyxit_transpose $pyxit_transpose --pyxit_fixed_size $pyxit_fixed_size --pyxit_interpolation $pyxit_interpolation --forest_n_estimators $forest_n_estimators --forest_max_features $forest_max_features --forest_min_samples_split $forest_min_samples_split --pyxit_n_subwindows $pyxit_n_subwindows --verbose");
            cytomine.addSoftwareParameter("host", "String", software.getId(), "$cytomineHosts$", true, 100);
            cytomine.addSoftwareParameter("publicKey", "String", software.getId(), "", true, 200);
            cytomine.addSoftwareParameter("privateKey", "String", software.getId(), "", true, 300);
            cytomine.addSoftwareParameter("cytomine_base_path", "String", software.getId(), "/api/", true, 400);
            cytomine.addSoftwareParameter("cytomine_software", "Number", software.getId(), "", true, 500);
            cytomine.addSoftwareParameter("cytomine_working_path", "String", software.getId(), "/home/julien/bigdata/cytomine/", true, 600);
            cytomine.addSoftwareParameter("cytomine_id_project", "Number", software.getId(), "", true, 700);
            cytomine.addSoftwareParameter("cytomine_annotation_projects", "Number", software.getId(), "", true, 800);
            cytomine.addSoftwareParameter("z", "Number", software.getId(), "0", true, 900);
            cytomine.addSoftwareParameter("cytomine_predict_terms", "Number", software.getId(), "", true, 1000);
            cytomine.addSoftwareParameter("cytomine_excluded_terms", "Number", software.getId(), "", true, 1100);
            cytomine.addSoftwareParameter("pyxit_target_width", "Number", software.getId(), "16", true, 1200);
            cytomine.addSoftwareParameter("pyxit_target_height", "Number", software.getId(), "16", true, 1300);
            cytomine.addSoftwareParameter("pyxit_colorspace", "Number", software.getId(), "2", true, 1400);
            cytomine.addSoftwareParameter("pyxit_n_jobs", "Number", software.getId(), "-1", true, 1500);
            cytomine.addSoftwareParameter("pyxit_save_to", "String", software.getId(), "/home/julien/bigdata/models/segmentation_tumor_model.pkl", true, 1600);
            cytomine.addSoftwareParameter("pyxit_transpose", "Boolean", software.getId(), "true", false, 1700);
            cytomine.addSoftwareParameter("pyxit_fixed_size", "Boolean", software.getId(), "true", false, 1800);
            cytomine.addSoftwareParameter("pyxit_interpolation", "Number", software.getId(), "2", false, 1900);
            cytomine.addSoftwareParameter("forest_n_estimators", "Number", software.getId(), "10", true, 2000);
            cytomine.addSoftwareParameter("forest_max_features", "Number", software.getId(), "1", true, 2100);
            cytomine.addSoftwareParameter("forest_min_samples_split", "Number", software.getId(), "1", true, 2200);
            cytomine.addSoftwareParameter("pyxit_n_subwindows", "Number", software.getId(), "10", true, 2300);
            cytomine.addSoftwareParameter("verbose", "String", software.getId(), " ", false, 2400);

        } catch (CytomineException e) {
            log.error(e);
        }
    }
}
