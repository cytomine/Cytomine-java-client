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
import be.cytomine.client.models.Project;
import be.cytomine.client.models.User;


public class UserCollection extends Collection<User> {

    public UserCollection() {
        this(0,0);
    }
    public UserCollection(int offset, int max) {
        super(User.class, max, offset);
    }

    public static UserCollection fetchMembersOfAProject(Project project) throws CytomineException {
        return fetchMembersOfAProject(project, false);
    }
    public static UserCollection fetchMembersOfAProject(Project project, boolean admin) throws CytomineException {
        return fetchMembersOfAProject(project, admin, false, 0,0);
    }
    public static UserCollection fetchMembersOfAProject(Project project, boolean admin, boolean online) throws CytomineException {
        return fetchMembersOfAProject(Cytomine.getInstance().getDefaultCytomineConnection(), project, admin, online, 0,0);
    }
    public static UserCollection fetchMembersOfAProject(Project project, boolean admin, boolean online, int offset, int max) throws CytomineException {
        return fetchMembersOfAProject(Cytomine.getInstance().getDefaultCytomineConnection(), project, admin, online, offset,max);
    }

    public static UserCollection fetchMembersOfAProject(CytomineConnection connection, Project project, boolean admin, boolean online) throws CytomineException {
        return fetchMembersOfAProject(connection, project, admin, online, 0,0);
    }

    public static UserCollection fetchMembersOfAProject(CytomineConnection connection, Project project, boolean admin, boolean online, int offset, int max) throws CytomineException {
        UserCollection uc = new UserCollection(max, offset);
        if(admin) uc.addFilter("admin","true");
        if(online) uc.addFilter("online","true");
        uc.addFilter("project",project.getId().toString());
        return (UserCollection)uc.fetch();
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {
        if (isFilterBy("project") && isFilterBy("admin")) {
            return "/api/project/" + getFilter("project") + "/admin.json";
        } else if (isFilterBy("project") && isFilterBy("online")) {
            return "/api/project/" + getFilter("project") + "/online/user.json";
        } else if (isFilterBy("project")) {
            return "/api/project/" + getFilter("project") + "/user.json";
        } else {
            return super.getJSONResourceURL();
        }
    }
}
