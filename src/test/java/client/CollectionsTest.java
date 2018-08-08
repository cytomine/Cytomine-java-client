package client;

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
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.*;
import org.reflections.Reflections;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CollectionsTest {

    private static final Logger log = Logger.getLogger(CollectionsTest.class);
    private static String host;
    private static String publicKey;
    private static String privateKey;

    @BeforeAll
    static void initAll() throws CytomineException {
        BasicConfigurator.configure();
        PropertyConfigurator.configure("log4j.properties");

        host = System.getProperty("host");
        assertNotNull(host, "host, publicKey, privateKey");
        publicKey = System.getProperty("publicKey");
        assertNotNull(publicKey, "host, publicKey, privateKey");
        privateKey = System.getProperty("privateKey");
        assertNotNull(privateKey, "host, publicKey, privateKey");

        log.info("Connection to cytomine...");
        Cytomine.connection(host,publicKey,privateKey);
    }

    @BeforeEach
    void init() {
    }

    @Test
    void testCollections() throws CytomineException {
        log.info("test list methods");

        Reflections reflections = new Reflections("be.cytomine.client.models");
        Set<Class<? extends Model>> classes = reflections.getSubTypesOf(Model.class);

        StringBuilder err = new StringBuilder();
        for (Class c : classes) {
            log.info(c.getSimpleName());
            //try {
                Collection.fetch(c, 0, 0);
            /*} catch (CytomineException e) {
                int code = e.getHttpCode();
                // 403 : Forbidden ==> not enough right : ok
                if (code != 403) {
                    err.append("Error " + code + " : Collection<" + c.getSimpleName() + "> : fetch\n");
                }
            }*/
        }
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }

}