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
import be.cytomine.client.collections.UserCollection;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTest {

    private static final Logger log = LogManager.getLogger(ProjectTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateProject() throws CytomineException {
        log.info("test create project");
        String name = Utils.getRandomString();
        Project p = new Project(name,Utils.getOntology()).save();
        assertEquals(name, p.get("name"), "name not the same used for the project creation");

        p = new Project().fetch(p.getId());
        assertEquals(name, p.get("name"), "fetched name not the same used for the project creation");

        p.set("name", name+"bis");
        p.update();
        assertEquals(name+"bis", p.get("name"), "Not the name used for the project update");

        p.delete();
        try {
            new Project().fetch(p.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateProjectIncorrect() throws CytomineException {
        log.info("test create incorrect project");
        Project project = Utils.getProject();

        try{
            new Project().save();
        } catch (CytomineException e){
            assertEquals(e.getHttpCode(), 400);
        }
    }

    @Test
    void testListProject() throws CytomineException {
        log.info("test list projects");
        Collection<Project> c = Collection.fetch(Project.class);

        log.info(c.size());
        assert c.size() > 0;
    }

    // project users
    @Test
    void testAddMembersInProject() throws CytomineException {
        log.info("test add user into a project");
        Project p = Utils.getNewProject();

        UserCollection c = new UserCollection();
        c.addFilter("project",p.getId().toString());
        c.fetch();
        int membersCount = c.size();

        p.addMember(Utils.getUser());

        c.fetch();
        assertEquals(membersCount+1, c.size(), "not expected size");
    }

    @Test
    void testRemoveMembersInProject() throws CytomineException {
        log.info("test remove user of a project");
        Project p = Utils.getNewProject();
        User user = Utils.getNewUser();
        p.addMember(user);

        UserCollection c = new UserCollection();
        c.addFilter("project",p.getId().toString());
        c.fetch();
        assertEquals(2, c.size(), "not expected size");

        p.removeMember(user);

        c.fetch();
        assertEquals(1, c.size(), "not expected size");
    }

    @Test
    void testAddAdminsInProject() throws CytomineException {
        log.info("test add user into a project");
        Project p = Utils.getNewProject();

        UserCollection c = new UserCollection();
        c.addFilter("project",p.getId().toString());
        c.fetch();
        int membersCount = c.size();

        p.addMember(Utils.getUser(),true);

        c.fetch();
        assertEquals(membersCount+1, c.size(), "not expected size");
    }

    @Test
    void testRemoveAdminsInProject() throws CytomineException {
        log.info("test remove user of a project");
        Project p = Utils.getNewProject();
        User user = Utils.getNewUser();
        p.addMember(user,true);

        UserCollection c = new UserCollection();
        c.addFilter("project",p.getId().toString());
        c.fetch();
        assertEquals(2, c.size(), "not expected size");

        p.removeMember(user);

        c.fetch();
        assertEquals(2, c.size(), "not expected size");

        p.removeMember(user,true);

        c.fetch();
        assertEquals(1, c.size(), "not expected size");
    }

    @Test
    void testListMembersOfProject() throws CytomineException {
        log.info("test list users into a project");

        UserCollection c = new UserCollection();
        c.addFilter("project",Utils.getProject().getId().toString());
        c.fetch();

        assertEquals(4, c.size(), "not expected size");

        c = UserCollection.fetchMembersOfAProject(Utils.getProject());
        assertEquals(4, c.size(), "not expected size");
    }

    @Test
    void testListAdminsOfProject() throws CytomineException {
        log.info("test list admins into a project");

        UserCollection c = new UserCollection();
        c.addFilter("project",Utils.getProject().getId().toString());
        c.addFilter("admin","true");
        c.fetch();

        assertEquals(2, c.size(), "not expected size");

        c = UserCollection.fetchMembersOfAProject(Utils.getProject(), true);
        assertEquals(2, c.size(), "not expected size");
    }

    @Test
    void testListConnectedUserInProject() throws CytomineException {
        log.info("test list connected users into a project");

        UserCollection c = new UserCollection();
        c.addFilter("project",Utils.getProject().getId().toString());
        c.addFilter("online","true");
        c.fetch();

        assertEquals(0, c.size(), "not expected size");

        c = UserCollection.fetchMembersOfAProject(Utils.getProject(), false, true);
        assertEquals(0, c.size(), "not expected size");
    }
}
