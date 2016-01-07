package be.cytomine.client.collections;

/*
 * Copyright (c) 2009-2016. Authors: see NOTICE file.
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

import be.cytomine.client.models.Property;
import org.json.simple.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: pierre
 * Date: 23/04/13
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class PropertyCollection extends Collection {

    public PropertyCollection(int offset, int max) {
        super(max, offset);
    }

    public String toURL() {
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
            return getJSONResourceURL();
        }
    }

    public String getDomainName() {
        return "property";
    }

    public Property get(int i) {
        Property property = new Property();
        Object item = list.get(i);
        property.setAttr((JSONObject) item);
        return property;
    }


    public String getJSONResourceURLWithFilter(String filter1Name) {
        String domainFix = filter1Name;
        if (filter1Name.contains(".")) {
            domainFix = "domain/" + filter1Name;
        }
        return "/api/" + domainFix + "/" + getFilter(filter1Name) + "/" + getDomainName() + ".json";
    }

    public String getJSONResourceURLWithFilter(String domain, String idDomain, String nameIdDomain) {

        return "/api/" + domain + "/" + getDomainName() + "/key.json?" + getFilter(nameIdDomain) + "=" + getFilter(idDomain);

    }
}
