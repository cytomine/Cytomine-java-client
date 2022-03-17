package be.cytomine.client.sample;
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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.StorageCollection;
import be.cytomine.client.models.UploadedFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UploadExample {

    private static final Logger log = LogManager.getLogger(UploadExample.class);


    // don't forget protocol into upload URL
    public static void upload(String uploadURL) throws Exception {

        try {

            log.info("Get Core URL");
            String coreURL = Cytomine.getInstance().getHost();

            log.info("Get storage list...");
            StorageCollection storages = new StorageCollection(0,0);
            storages = (StorageCollection) storages.fetch();

            Collection<UploadedFile> c = Collection.fetch(UploadedFile.class);
            long size = c.size();

            Long idProject = null;

            Cytomine.getInstance().uploadImage(uploadURL, "./logo.png", idProject, storages.get(0).getId(), coreURL, null, false);

            c = Collection.fetch(UploadedFile.class);

            assert c.size() > size;

        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
}
