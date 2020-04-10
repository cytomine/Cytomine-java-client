package be.cytomine.client.collections;

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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Job;
import be.cytomine.client.models.JobParameter;

public class JobParameterCollection extends Collection<JobParameter> {

    public JobParameterCollection() {
        this(0,0);
    }
    public JobParameterCollection(int offset, int max) {
        super(JobParameter.class, max, offset);
    }

    public static JobParameterCollection fetchByJob(Job job) throws CytomineException {
        return fetchByJob(Cytomine.getInstance().getDefaultCytomineConnection(), job);
    }
    public static JobParameterCollection fetchByJob(CytomineConnection connection, Job job) throws CytomineException {
        return fetchByJob(connection, job, 0,0);
    }
    public static JobParameterCollection fetchByJob(Job job, int offset, int max) throws CytomineException {
        return fetchByJob(Cytomine.getInstance().getDefaultCytomineConnection(), job, offset,max);
    }
    public static JobParameterCollection fetchByJob(CytomineConnection connection, Job job, int offset, int max) throws CytomineException {
        return (JobParameterCollection) new JobParameterCollection(max, offset).fetchWithFilter(connection, Job.class, job.getId(), offset, max);
    }


    //TODO remove this when rest url normalized
    public String getDomainName() throws CytomineException {
        if(isFilterBy("job")) return "parameter";
        return "jobparameter";
    }
}
