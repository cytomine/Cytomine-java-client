package be.cytomine.client.models;

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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import org.json.simple.JSONObject;

import java.util.Map;

public class Property extends Model<Property> {

    public Property() {}
    public Property(Model model) {
        this(model, null, null);
    }
    public Property(Model model, String key, String value) {
        this(model.getClass().getSimpleName().toLowerCase(),model.getId(), key, value);
    }
    public Property(String domain, Long idDomain){
        this(domain, idDomain, null);
    }

    public Property(String domain, Long idDomain, String key) {
        this(domain, idDomain, key, null);
    }
    public Property(String domain, Long idDomain, String key, String value) {
        addFilter(domain,idDomain.toString());
        addFilter("key",key);
        set("key", key);
        set("value", value);
    }

    @Override
    public Property fetch(Long id) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(),id);
    }
    public Property fetch(String id) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(),id);
    }
    @Override
    public Property fetch(CytomineConnection connection, Long id) throws CytomineException {
        throw new CytomineException(400,"Cannot fetch property with an id, fetch it with its key");
    }
    public Property fetch(CytomineConnection connection, String id) throws CytomineException {
        this.set("key", id);
        addFilter("key",id);
        JSONObject json = connection.doGet(this.toURL());
        this.setAttr(json);
        return this;
    }

    @Override
    public String getJSONResourceURL() {
        Long id = getId();
        String base = "/api/";
        //base += "domain/";

        //hack to fit url until refactoring of urls
        if(getFilter("key")!=null && get("value") != null) filters.remove("key");

        base += getFilterPrefix();
        base += getDomainName();
        if(id!= null) {
            base += "/" + id + ".json?";
        } else {
            base += ".json?";
        }

        for (Map.Entry<String, String> param : params.entrySet()) {
            base = base + param.getKey() + "=" + param.getValue() + "&";
        }
        base = base.substring(0, base.length() - 1);
        return base;
    }

    /*public String toURL() {
        Long id = (Long) get("id");
        Long domainIdent = (Long) get("domainIdent");
        String domain = (String) get("domain");
        String key = (String) get("key");

        if (id != null && domainIdent != null && domain != null) {
            return getJSONResourceURL(id, domainIdent, domain);
        } else if (domainIdent != null && domain != null && key != null) {
            return getJSONResourceURL(domainIdent, domain, key);
        } else if (domainIdent != null && domain != null) {
            return getJSONResourceURL(domainIdent, domain);
        } else {
            return getJSONResourceURL();
        }
    }

    public String getJSONResourceURL(Long id, Long domainIdent, String domain) {
        String domainFix = domain;
        if (domain.contains(".")) {
            domainFix = "domain/" + domain;
        }
        if (params.isEmpty()) {
            return "/api/" + domainFix + "/" + domainIdent + "/property/" + id + ".json";
        } else {
            String base = "/api/" + domainFix + "/" + domainIdent + "/property/" + id + ".json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }


    public String getJSONResourceURL(Long domainIdent, String domain) {
        String domainFix = domain;
        if (domain.contains(".")) {
            domainFix = "domain/" + domain;
        }
        if (params.isEmpty()) {
            return "/api/" + domainFix + "/" + domainIdent + "/property.json";
        } else {
            String base = "/api/" + domainFix + "/" + domainIdent + "/property.json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }

    public String getJSONResourceURL(Long domainIdent, String domain, String key) {
        String domainFix = domain;
        if (domain.contains(".")) {
            domainFix = "domain/" + domain;
        }
        String base = "/api/" + domainFix + "/" + domainIdent + "/key/"+key+"/property.json";
        return base;
    }*/
}
