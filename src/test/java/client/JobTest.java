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
import be.cytomine.client.collections.JobCollection;
import be.cytomine.client.models.Job;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.Software;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobTest {

    private static final Logger log = LogManager.getLogger(JobTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateJob() throws CytomineException {
        log.info("test create software_parameter");
        Software software = Utils.getSoftware();
        Project project = Utils.getProject();
        Job j = new Job(software, project).save();
        assertEquals(project.getId(), j.get("project"), "project not the same used for the job creation");

        j = new Job().fetch(j.getId());
        assertEquals(project.getId(), j.get("project"), "fetched project not the same used for the job creation");

        j.delete();
        try {
            new Job().fetch(j.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateJobIncorrect() throws CytomineException {
        log.info("test create incorrect job");

        try {
            new Job().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
        Software software = Utils.getSoftware();

        try {
            Job j = new Job();
            j.set("software", software.getId());
            j.save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }
    @Test
    void testListJobs() throws CytomineException {
        log.info("test list jobs");
        Collection<Job> c = Collection.fetch(Job.class);

        log.info(c.size());
    }

    @Test
    void testListJobsOfSoftware() throws CytomineException {
        log.info("test list jobs of a software");
        Software software = Utils.getSoftware();
        JobCollection jc = new JobCollection();
        jc.addFilter("software", software.getId().toString());
        jc.fetch();

        int size = jc.size();
        log.info(jc.size());

        jc = JobCollection.fetchBySoftware(software);
        assertEquals(size,jc.size());
        log.info(jc.size());
    }

    @Test
    void testListJobsOfProject() throws CytomineException {
        log.info("test list jobs of a project");
        Project project = Utils.getProject();
        JobCollection jc = new JobCollection();
        jc.addFilter("project", project.getId().toString());
        jc.fetch();

        int size = jc.size();
        log.info(jc.size());

        jc = JobCollection.fetchByProject(project);
        assertEquals(size,jc.size());
        log.info(jc.size());
    }

    @Test
    void testListJobsOfProjectAndSoftware() throws CytomineException {
        log.info("test list jobs of a project and a software");
        Software software = Utils.getSoftware();
        Project project = Utils.getProject();

        JobCollection jc = new JobCollection();
        jc.addFilter("software", software.getId().toString());
        jc.addFilter("project", project.getId().toString());
        jc.fetch();

        int size = jc.size();
        log.info(jc.size());

        jc = JobCollection.fetchByProjectAndSoftware(project, software);
        assertEquals(size,jc.size());
        log.info(jc.size());
    }

}
