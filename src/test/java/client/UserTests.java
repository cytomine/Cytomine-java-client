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
import be.cytomine.client.models.User;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    private static final Logger log = Logger.getLogger(UserTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateUser() throws CytomineException {
        log.info("test create user");
        String username = Utils.getRandomString();
        User u = new User(username,Utils.getRandomString(),Utils.getRandomString(),"test@test.be",Utils.getRandomString()).save();

        u = new User().fetch(u.getId());
        assertEquals(username, u.get("username"), "fetched user not the same used for the user creation");

        u.set("firstname", username+"bis");
        u.update();
        assertEquals(username+"bis", u.get("firstname"), "Not the name used for the user update");
    }

    @Test
    void testGetCurrentUser() throws CytomineException {
        log.info("test get current user");

        User user = User.getCurrent();

        assertEquals(Utils.getPublicKey(), user.get("publicKey"));
    }

    @Test
    void testListUser() throws CytomineException {
        log.info("test list users");
        Collection<User> c = Collection.fetch(User.class);

        log.info(c.size());
        assert c.size() > 0;

        UserCollection uc = new UserCollection();
        uc.fetch();

        log.info(uc.size());
        assert uc.size() > 0;
    }
}
