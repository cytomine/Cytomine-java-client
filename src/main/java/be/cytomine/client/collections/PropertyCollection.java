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
import be.cytomine.client.models.Model;
import be.cytomine.client.models.Property;

public class PropertyCollection extends Collection {

    public PropertyCollection() {
        this(0,0);
    }
    public PropertyCollection(int offset, int max) {
        super(Property.class, max, offset);
    }


    public static PropertyCollection fetchByAssociatedDomain(Model domain) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain, 0,0);
    }
    public static PropertyCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain) throws CytomineException {
        return fetchByAssociatedDomain(connection, domain, 0,0);
    }

    public static PropertyCollection fetchByAssociatedDomain(Model domain, int offset, int max) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain, offset,max);
    }

    public static PropertyCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain, int offset, int max) throws CytomineException {
        return (PropertyCollection) new PropertyCollection(max, offset).fetchWithFilter(connection, domain.getClass(), domain.getId(), offset, max);
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {
        if (isFilterBy("annotation") && isFilterBy("idDomain") && isFilterBy("nameIdDomain")) {
            return getJSONResourceURLWithFilter("annotation", "idDomain", "nameIdDomain");
        } else if (isFilterBy("imageinstance") && isFilterBy("idDomain") && isFilterBy("nameIdDomain")) {
            return getJSONResourceURLWithFilter("imageinstance", "idDomain", "nameIdDomain");

        } else if (isFilterBy("annotation") && isFilterBy("key")) {
            return getJSONResourceURLWithFilter("annotation", "key");
        } else if (isFilterBy("project") && isFilterBy("key")) {
            return getJSONResourceURLWithFilter("project", "key");
        } else if (isFilterBy("imageinstance") && isFilterBy("key")) {
            return getJSONResourceURLWithFilter("imageinstance", "key");
        } else if (isFilterBy("annotation")) {
            return getJSONResourceURLWithFilter("annotation");
        } else if (isFilterBy("project")) {
            return getJSONResourceURLWithFilter("project");
        } else if (isFilterBy("imageinstance")) {
            return getJSONResourceURLWithFilter("imageinstance");
        } else if (isFilterBy("domainClassName")) {
            return getJSONResourceURLWithFilter(getFilter("domainClassName"));
        } else {
            return super.getJSONResourceURL();
        }
    }
    private String getJSONResourceURLWithFilter(String filter1Name) throws CytomineException {
        return "/api/" + filter1Name + "/" + getFilter(filter1Name) + "/" + getDomainName() + ".json";
    }

    private String getJSONResourceURLWithFilter(String filter1Name, String filter2Name) throws CytomineException {
        return "/api/" + filter1Name + "/" + getFilter(filter1Name) + "/" + filter2Name + "/" + getFilter(filter2Name) + "/" + getDomainName() + ".json";
    }

    private String getJSONResourceURLWithFilter(String domain, String idDomain, String nameIdDomain) throws CytomineException {
        return "/api/" + domain + "/" + getDomainName() + "/key.json?" + getFilter(nameIdDomain) + "=" + getFilter(idDomain);
    }
}
