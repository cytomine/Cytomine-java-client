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
import be.cytomine.client.collections.JobDataCollection;
import be.cytomine.client.collections.SoftwareParameterCollection;
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobDataTests {

    private static final Logger log = Logger.getLogger(JobDataTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateJobData() throws CytomineException {
        log.info("test create job_data");
        String name = Utils.getRandomString();
        Job job = Utils.getJob();
        JobData jd = new JobData(job, name, "value").save();
        assertEquals(name, jd.get("key"), "key not the same used for the job_data creation");

        jd = new JobData().fetch(jd.getId());
        assertEquals(name, jd.get("key"), "fetched key not the same used for the job_data creation");

        jd.set("key", name+"bis");
        jd.update();
        assertEquals(name+"bis", jd.get("key"), "Not the key used for the job_data update");

        jd.delete();
        try {
            new JobData().fetch(jd.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateJobDataIncorrect() throws CytomineException {
        log.info("test create incorrect job_data");

        try {
            new JobData().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
        Job job = Utils.getJob();

        try {
            JobData jd = new JobData();
            jd.set("job", job.getId());
            jd.save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testUploadJobData() throws CytomineException, IOException {
        log.info("test upload job_data");
        String name = Utils.getRandomString();
        Job job = Utils.getJob();
        JobData jd = new JobData(job, name, "value").save();

        jd.uploadJobData(Utils.getFile());

        File f = jd.downloadJobData("/tmp/"+jd.getStr("filename"));

        byte[] f1 = Files.readAllBytes(Utils.getFile().toPath());
        byte[] f2 = Files.readAllBytes(f.toPath());

        assertEquals(f1.length,f2.length);
        for(int i = 0;i<f1.length;i++){
            assertEquals(f1[0],f2[0]);
        }
    }

    @Test
    void testListJobData() throws CytomineException {
        log.info("test list job_data");
        Collection<JobData> c = Collection.fetch(JobData.class);

        log.info(c.size());
    }

    @Test
    void testListJobDataOfJob() throws CytomineException {
        log.info("test list job_data by job");
        Job job = Utils.getJob();
        Collection<JobData> jdc = Collection.fetch(JobData.class);
        jdc.addFilter("job", job.getId().toString());
        jdc.fetch();

        int size = jdc.size();
        log.info(jdc.size());

        jdc = JobDataCollection.fetchByJob(job);
        assertEquals(size,jdc.size());
        log.info(jdc.size());
    }
}
