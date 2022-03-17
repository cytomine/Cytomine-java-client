package be.cytomine.client.sample;
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
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ProjectCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProjectExample {

    private static final Logger log = LogManager.getLogger(ProjectExample.class);


    public static void listProject(Cytomine cytomine) throws Exception {

        try {
            log.info("Get project list...");
            ProjectCollection projects = new ProjectCollection(0,0);
            projects = (ProjectCollection) projects.fetch();

            log.info("projects=" + projects.getList());

            for (int i = 0; i < projects.size(); i++) {
                log.info("projects=" + projects.get(i));
            }

            log.info("############################################");
            projects = (ProjectCollection) projects.fetch(0,5);
            for (int i = 0; i < projects.size(); i++) {
                log.info("projects=" + projects.get(i));
            }
            projects = (ProjectCollection) projects.fetchNextPage();
            for (int i = 0; i < projects.size(); i++) {
                log.info("projects=" + projects.get(i));
            }
            log.info("############################################");

            do {

                for (int i = 0; i < projects.size(); i++) {
                    log.info("projects=" + projects.get(i));
                }


            } while ((projects.fetchNextPage()).size() > 0);

        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();

        }
    }
}
