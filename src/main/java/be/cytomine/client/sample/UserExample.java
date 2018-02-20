package be.cytomine.client.sample;
/*
 * Copyright (c) 2009-2018. Authors: see NOTICE file.
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
import be.cytomine.client.models.User;

import java.util.Map;

public class UserExample {

    public static void testAddUser(Cytomine cytomine) throws Exception {
        User user = cytomine.addUser("gigaraph", "test", "test", "test@test.com", "xxxxxx");
    }

    public static void testRole(Cytomine cytomine) throws Exception {
        //ROLE_USER, ROLE_GHEST, ROLE_ADMIN
        Map<String, Long> roles = cytomine.getRoleMap();
        System.out.println(roles);
        User user = cytomine.getUser(8579749L);
        cytomine.addRole(8579749L, roles.get("ROLE_USER"));
        cytomine.deleteRole(8579749L, roles.get("ROLE_USER"));
    }

}
