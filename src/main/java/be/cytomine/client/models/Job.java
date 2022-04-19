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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;

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

        public int getValue() {
            return this.valueOfJob;
        }

    }

    public Job(){}
    public Job(Software software, Project project){
        this(software.getId(), project.getId());
    }
    public Job(Long softwareId, Long projectId){
        set("project", projectId);
        set("software", softwareId);
    }

    public void execute() throws CytomineException {
        Cytomine.getInstance().getDefaultCytomineConnection().doPost("/api/job/"+get("id")+"/execute.json", "");
    }

    public Job changeStatus(JobStatus status, int progress) throws CytomineException {
        return changeStatus(status.getValue(), progress);
    }
    public Job changeStatus(JobStatus status, int progress, String comment) throws CytomineException {
        return changeStatus(status.getValue(), progress,comment);
    }
    public Job changeStatus(int status, int progress) throws CytomineException {
        return this.changeStatus(status, progress, null);
    }

    public Job changeStatus(int status, int progress, String comment) throws CytomineException {
        this.set("progress", progress);
        this.set("status", status);
        this.set("statusComment", comment);
        return this.update();
    }
}
