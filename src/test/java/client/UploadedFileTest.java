package client;

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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.AbstractImage;
import be.cytomine.client.models.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadedFileTest {

    private static final Logger log = LogManager.getLogger(UploadedFileTest.class);

    @BeforeAll
    static void init() throws CytomineException {
        Utils.connect();
    }

    @Test
    void testCreateUploadedFile() throws CytomineException {
        log.info("test create uploaded_file");
        String name = Utils.getRandomString();
        UploadedFile uf = new UploadedFile("originalFilename", "realFilename", Utils.getFile(), 0L, "ext", "contentType", new ArrayList(), new ArrayList(), Utils.getUser(), UploadedFile.Status.UPLOADED, null).save();
        assertEquals(UploadedFile.Status.UPLOADED.getCode(), (int)uf.getInt("status"), "status not the same used for the uploaded_file creation");

        uf = new UploadedFile().fetch(uf.getId());
        assertEquals(UploadedFile.Status.UPLOADED.getCode(), (int)uf.getInt("status"), "fetched status not the same used for the uploaded_file creation");

        uf.set("status", UploadedFile.Status.CONVERTED.getCode());
        uf.update();
        assertEquals(UploadedFile.Status.CONVERTED.getCode(), (int)uf.getInt("status"), "Not the same status used for the uploaded_file update");

        uf.changeStatus(UploadedFile.Status.DEPLOYED);
        assertEquals(UploadedFile.Status.DEPLOYED.getCode(), (int)uf.getInt("status"), "Not the same status used for the uploaded_file update");

        uf.delete();
        try {
            new UploadedFile().fetch(uf.getId());
            assert false;
        } catch (CytomineException e) {
            assertEquals(e.getHttpCode(), 404);
        }
    }

    @Test
    void testCreateUploadedFileIncorrect() throws CytomineException {
        log.info("test create incorrect uploaded_file");

        try {
            new UploadedFile().save();
            assert false;
        } catch (CytomineException e) {
            assertEquals(400, e.getHttpCode());
        }
    }

    @Test
    void testGetUploadedFileByAbstractImage() throws CytomineException {
        log.info("test get an uploadedFile by its associated abstractImage");
        String name = Utils.getRandomString();
        AbstractImage ai = new AbstractImage(name, "image/tiff").save();
        UploadedFile uf = new UploadedFile("originalFilename", "realFilename", Utils.getFile(), 0L, "ext", "contentType", new ArrayList(), new ArrayList(), Utils.getUser(), UploadedFile.Status.UPLOADED, null);
        uf.set("image", ai.getId());
        uf.save();

        uf = new UploadedFile().fetch(uf.getId());
        assertEquals(ai.getId(), uf.getLong("image"), "fetched image not the same used for the uploaded_file creation");

        Long idUF = uf.getId();

        uf = UploadedFile.getByAbstractImage(ai);
        assertEquals(idUF, uf.getId(), "not the expected uploaded file");
    }

    @Test
    void testListUploadedFiles() throws CytomineException {
        log.info("test list uploaded_files");
        Collection<UploadedFile> c = Collection.fetch(UploadedFile.class);

        log.info(c.size());
    }

}
