package client;

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

import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.SoftwareParameterCollection;
import be.cytomine.client.models.Software;
import be.cytomine.client.models.SoftwareParameter;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoftwareParameterTests {

    private static final Logger log = Logger.getLogger(SoftwareParameterTests.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateSoftwareParameter() throws CytomineException {
        log.info("test create software_parameter");
        String name = Utils.getRandomString();
        Software software = Utils.getSoftware();
        SoftwareParameter sp = new SoftwareParameter(name, "", software.getId(), "", true, 0).save();
        assertEquals(name, sp.get("name"), "name not the same used for the software_parameter creation");

        sp = new SoftwareParameter().fetch(sp.getId());
        assertEquals(name, sp.get("name"), "fetched name not the same used for the software_parameter creation");

        sp.set("name", name+"bis");
        sp.update();
        assertEquals(name+"bis", sp.get("name"), "Not the name used for the software_parameter update");

        sp.delete();
        try {
            new SoftwareParameter().fetch(sp.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateSoftwareParameterIncorrect() throws CytomineException {
        log.info("test create incorrect software_parameter");

        try {
            new SoftwareParameter().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
        Software software = Utils.getSoftware();

        try {
            SoftwareParameter sp = new SoftwareParameter();
            sp.set("software", software.getId());
            sp.save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }
    @Test
    void testListSoftwareParameters() throws CytomineException {
        log.info("test list software_parameters");
        Collection<SoftwareParameter> c = Collection.fetch(SoftwareParameter.class);

        log.info(c.size());
    }

    @Test
    void testListSoftwareParametersOfSoftware() throws CytomineException {
        log.info("test list software_parameters of a software");
        Software software = Utils.getSoftware();
        SoftwareParameterCollection spc = new SoftwareParameterCollection();
        spc.addFilter("software", software.getId().toString());
        spc.fetch();

        int size = spc.size();
        log.info(spc.size());

        spc = SoftwareParameterCollection.fetchBySoftware(software);
        assertEquals(size, spc.size());
        log.info(spc.size());
    }
}
