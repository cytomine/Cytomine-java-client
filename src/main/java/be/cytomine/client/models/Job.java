package be.cytomine.client.models;

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

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class Job extends Model<Job> {

    public enum JobStatus{
        NOTLAUNCH  (0),
        INQUEUE  (1),
        RUNNING  (2),
        SUCCESS  (3),
        FAILED (4),
        INDETERMINATE  (5),
        WAIT  (6),
        PREVIEWED  (7),
        KILLED (8);

        private int valueOfJob;

        JobStatus(int value){this.valueOfJob=value;}
    }

    public int getVal(JobStatus js)
    {
        return js.valueOfJob;
    }

    public Job(){}
    public Job(Software software, Project project){
        this(software.getId(), project.getId());
    }
    public Job(Long softwareId, Long projectId){
        set("project", projectId);
        set("software", softwareId);
    }

    public Job changeStatus(Long id, int status, int progress) throws CytomineException {
        return this.changeStatus(id, status, progress, null);
    }

    public Job changeStatus(Long id, int status, int progress, String comment) throws CytomineException {
        Job job = new Job().fetch(id);
        job.set("progress", progress);
        job.set("status", status);
        job.set("statusComment", comment);
        return job.update();
    }
}
