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
import be.cytomine.client.models.SoftwareParameter;

public class SoftwareParameterCollection extends Collection<SoftwareParameter> {

    public SoftwareParameterCollection() {
        this(0,0);
    }
    public SoftwareParameterCollection(int offset, int max) {
        super(SoftwareParameter.class, max, offset);
    }

    public static SoftwareParameterCollection fetchBySoftware(Software software) throws CytomineException {
        return fetchBySoftware(Cytomine.getInstance().getDefaultCytomineConnection(), software, 0,0);
    }
    public static SoftwareParameterCollection fetchBySoftware(CytomineConnection connection, Software software) throws CytomineException {
        return fetchBySoftware(connection, software, 0,0);
    }

    public static SoftwareParameterCollection fetchBySoftware(Software software, int offset, int max) throws CytomineException {
        return fetchBySoftware(Cytomine.getInstance().getDefaultCytomineConnection(), software, offset,max);
    }

    public static SoftwareParameterCollection fetchBySoftware(CytomineConnection connection, Software software, int offset, int max) throws CytomineException {
        return (SoftwareParameterCollection) new SoftwareParameterCollection(max, offset).fetchWithFilter(connection, Software.class, software.getId(), offset, max);
    }

    //TODO remove this when rest url normalized
    public String getDomainName() throws CytomineException {
        if(isFilterBy("software")) return "parameter";
        return "softwareparameter";
    }
}
