package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2018. Authors: see NOTICE file.
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

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class Description extends Model<Description> implements ICompositePrimaryKey<Description> {

    public String getDomainName() {
        return "description";
    }

    public String toURL() {
        Long domainIdent = getLong("domainIdent");
        String domainClassName = getStr("domainClassName");

        return getJSONResourceURL(domainIdent, domainClassName);
    }

    public String getJSONResourceURL(Long domainIdent, String domainClassName) {
        if (params.isEmpty()) {
            return "/api/domain/" + domainClassName + "/" + domainIdent + "/description.json";
        } else {
            String base = "/api/domain/" + domainClassName + "/" + domainIdent + "/description.json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }

    @Override
    public Description fetch(String domainClassName, String domainIdent) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(),domainIdent, domainClassName);
    }
    @Override
    public Description fetch(CytomineConnection connection, String domainClassName, String domainIdent) throws CytomineException {
        this.set("domainIdent", domainIdent);
        this.set("domainClassName", domainClassName);
        JSONObject json = connection.doGet(this.toURL());
        this.setAttr(json);
        return this;
    }
}
