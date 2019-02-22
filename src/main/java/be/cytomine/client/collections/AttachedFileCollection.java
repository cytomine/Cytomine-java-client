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
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.AttachedFile;
import be.cytomine.client.models.Model;

import java.util.Map;

public class AttachedFileCollection extends Collection {

    public AttachedFileCollection() {
        this(0,0);
    }
    public AttachedFileCollection(int offset, int max) {
        super(AttachedFile.class, max, offset);
    }

    public AttachedFileCollection(Model model) {
        this(model, 0,0);
    }
    public AttachedFileCollection(Model model, int offset, int max) {
        super(AttachedFile.class, max, offset);
        this.addFilter("domainClassName", Cytomine.convertDomainName(model.getClass().getSimpleName().toLowerCase()));
        this.addFilter("domainIdent", model.getId().toString());
    }


    @Override
    public String getJSONResourceURL() {
        String base;
        if(isFilterBy("domainClassName")) {
            base = "/api/domain/" + getFilter("domainClassName") + "/" + getFilter("domainIdent") + "/attachedfile.json";
        } else {
            base = "/api/attachedfile.json";
        }
        return base;
    }

/*
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
    */
}
