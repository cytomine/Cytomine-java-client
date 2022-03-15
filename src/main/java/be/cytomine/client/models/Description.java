package be.cytomine.client.models;

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
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import org.json.simple.JSONObject;

import java.util.Map;

public class Description extends Model<Description> implements ICompositePrimaryKey<Description> {

    public Description() {}
    public Description(Model model) {
        this(model, null);
    }
    public Description(Model model, String description) {
        this(model.getClass().getSimpleName().toLowerCase(),model.getId(), description);
    }
    public Description(String domain, Long idDomain){
        this(domain, idDomain, null);
    }

    public Description(String domain, Long idDomain, String description) {
        // TODO : remove the call to convertDomainName when rest url of core are normalized
        set("domainClassName",Cytomine.convertDomainName(domain));
        set("domainIdent",idDomain.toString());
        set("data", description);
    }

    public String getDomainName() {
        return "description";
    }

    public String toURL() {
        Long domainIdent = getLong("domainIdent");
        String domainClassName = getStr("domainClassName");

        return getJSONResourceURL(domainIdent, domainClassName);
    }

    public String getJSONResourceURL(Long domainIdent, String domainClassName) {
        String base = "/api/domain/" + domainClassName + "/" + domainIdent + "/description.json?";
        for (Map.Entry<String, String> param : params.entrySet()) {
            base = base + param.getKey() + "=" + param.getValue() + "&";
        }
        base = base.substring(0, base.length() - 1);
        return base;
    }

    public Description fetch(Model model) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(),model);
    }
    public Description fetch(CytomineConnection connection, Model model) throws CytomineException {
        return this.fetch(connection, model.getClass().getSimpleName().toLowerCase(), model.getId().toString());
    }
    @Override
    public Description fetch(String domainClassName, String domainIdent) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(),domainClassName, domainIdent);
    }
    @Override
    public Description fetch(CytomineConnection connection, String domainClassName, String domainIdent) throws CytomineException {
        this.set("domainIdent", domainIdent);
        this.set("domainClassName", Cytomine.convertDomainName(domainClassName));
        JSONObject json = connection.doGet(this.toURL());
        this.setAttr(json);
        return this;
    }
}
