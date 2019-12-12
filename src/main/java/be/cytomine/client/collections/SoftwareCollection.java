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
import be.cytomine.client.models.Software;
import be.cytomine.client.models.SoftwareUserRepository;
import org.json.simple.JSONObject;

public class SoftwareCollection extends Collection<Software> {

    public SoftwareCollection(int offset, int max) {
        super(Software.class, max, offset);
    }


    public static SoftwareCollection fetchBySoftwareUserRepository(Long idSoftwareUserRepository) throws CytomineException {

        return fetchBySoftwareUserRepository((Cytomine.getInstance().getDefaultCytomineConnection()), idSoftwareUserRepository, 0,0);

    }
    public static SoftwareCollection fetchBySoftwareUserRepository(CytomineConnection connection, Long idSoftwareUserRepository) throws CytomineException {
        return fetchBySoftwareUserRepository(connection, idSoftwareUserRepository, 0,0);
    }

    public static SoftwareCollection fetchBySoftwareUserRepository(Long idSoftwareUserRepository, int offset, int max) throws CytomineException {
        return fetchBySoftwareUserRepository(Cytomine.getInstance().getDefaultCytomineConnection(), idSoftwareUserRepository, offset,max);
    }

    public static SoftwareCollection fetchBySoftwareUserRepository(CytomineConnection connection, Long idSoftwareUserRepository, int offset, int max) throws CytomineException {
        return (SoftwareCollection) new SoftwareCollection(max, offset).fetchWithFilter(connection, SoftwareUserRepository.class, idSoftwareUserRepository, offset, max);
    }
}
