package be.cytomine.client.collections;

/*
 * Copyright (c) 2009-2019. Authors: see NOTICE file.
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
import be.cytomine.client.models.Model;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.Software;

import java.util.Map;


public class JobCollection extends Collection<Job> {

    public JobCollection() {
        this(0, 0);
    }
    public JobCollection(int offset, int max) {
        super(Job.class, max, offset);
    }


    public static JobCollection fetchBySoftware(Software software) throws CytomineException {
        return fetchBySoftware(Cytomine.getInstance().getDefaultCytomineConnection(), software);
    }
    public static JobCollection fetchBySoftware(CytomineConnection connection, Software software) throws CytomineException {
        return fetchByDomain(connection, software, 0,0);
    }
    public static JobCollection fetchBySoftware(Software software, int offset, int max) throws CytomineException {
        return fetchByDomain(Cytomine.getInstance().getDefaultCytomineConnection(), software, offset,max);
    }

    public static JobCollection fetchByProject(Project project) throws CytomineException {
        return fetchByProject(Cytomine.getInstance().getDefaultCytomineConnection(), project);
    }
    public static JobCollection fetchByProject(CytomineConnection connection, Project project) throws CytomineException {
        return fetchByDomain(connection, project, 0,0);
    }
    public static JobCollection fetchByProject(Project project, int offset, int max) throws CytomineException {
        return fetchByDomain(Cytomine.getInstance().getDefaultCytomineConnection(), project, offset,max);
    }

    private static JobCollection fetchByDomain(CytomineConnection connection, Model domain, int offset, int max) throws CytomineException {
        return (JobCollection) new JobCollection(max, offset).fetchWithFilter(connection, domain.getClass(), domain.getId(), offset, max);
    }

    public static JobCollection fetchByProjectAndSoftware(Project project, Software software) throws CytomineException {
        return fetchByProjectAndSoftware(Cytomine.getInstance().getDefaultCytomineConnection(), project, software);
    }
    public static JobCollection fetchByProjectAndSoftware(CytomineConnection connection, Project project, Software software) throws CytomineException {
        return fetchByProjectAndSoftware(connection, project, software, 0,0);
    }
    public static JobCollection fetchByProjectAndSoftware(Project project, Software software, int offset, int max) throws CytomineException {
        return fetchByProjectAndSoftware(Cytomine.getInstance().getDefaultCytomineConnection(), project, software, offset,max);
    }
    public static JobCollection fetchByProjectAndSoftware(CytomineConnection connection, Project project, Software software, int offset, int max) throws CytomineException {
        JobCollection sc = new JobCollection(max, offset);
        sc.addFilter(project.getDomainName(), project.getId().toString());
        sc.addFilter(software.getDomainName(), software.getId().toString());
        return (JobCollection)sc.fetch();
    }




    //TODO remove when rest url are normalized
    protected String getJSONResourceURL() throws CytomineException {
        return "/api" + "/" + getDomainName() + ".json";
    }

    @Override
    protected Map<String, String> getParams() {
        return getFilters();
    }
}
