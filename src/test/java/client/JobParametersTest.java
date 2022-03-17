package client;

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

import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.JobParameterCollection;
import be.cytomine.client.models.Job;
import be.cytomine.client.models.JobParameter;
import be.cytomine.client.models.Software;
import be.cytomine.client.models.SoftwareParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobParametersTest {

    private static final Logger log = LogManager.getLogger(JobParametersTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateJobParameter() throws CytomineException {
        log.info("test create job_parameter");
        String name = Utils.getRandomString();
        Job job = Utils.getJob();
        Software software = Utils.getSoftware();
        //TODO: clean that! update software test with new software architecture!

        SoftwareParameter sp = new SoftwareParameter("", "", new Long(0), "", true, 0, "", "","", true, true, "", "","").save();
        String value = "value";
        JobParameter jp = new JobParameter(job, sp ,value).save();
        assertEquals(value, jp.get("value"), "value not the same used for the job_parameter creation");

        jp = new JobParameter().fetch(jp.getId());
        assertEquals(value, jp.get("value"), "fetched value not the same used for the job_parameter creation");

        jp.set("value", value+"bis");
        jp.update();
        assertEquals(value+"bis", jp.get("value"), "Not the value used for the job_parameter update");

        jp.delete();
        try {
            new JobParameter().fetch(jp.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateJobParameterIncorrect() throws CytomineException {
        log.info("test create incorrect job_parameter");

        try {
            new JobParameter().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
        Job job = Utils.getJob();

        try {
            JobParameter sp = new JobParameter();
            sp.set("job", job.getId());
            sp.save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testListJobParameters() throws CytomineException {
        log.info("test list job_parameters");
        Collection<JobParameter> c = Collection.fetch(JobParameter.class);

        log.info(c.size());
    }

    @Test
    void testListJobParametersOfJob() throws CytomineException {
        log.info("test list job_parameters of a job");

        Job job = Utils.getJob();
        JobParameterCollection jpc = new JobParameterCollection();
        jpc.addFilter("job", job.getId().toString());
        jpc.fetch();

        int size = jpc.size();
        log.info(jpc.size());

        jpc = JobParameterCollection.fetchByJob(job);
        assertEquals(size,jpc.size());
        log.info(jpc.size());
    }
}
