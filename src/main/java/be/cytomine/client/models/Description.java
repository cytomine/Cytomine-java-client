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

    public Description() {
    }

    public Description(String domainName, Long domainIdent) {
        this.set("domainIdent", domainIdent);
        this.set("domainClassName", domainName);
        this.addFilter(domainName, domainIdent.toString());
    }

    public Description(Model model) {
        this(model.getDomainName(), model.getId());
    }

    public Description(String domainName, Long domainIdent, String text) {
        this(domainName, domainIdent);
        this.set("data", text);
    }

    public Description(Model model, String text) {
        this(model.getDomainName(), model.getId(), text);
    }


    @Override
    public Description fetch(String domainName, String domainIdent) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(), domainIdent, domainName);
    }

    @Override
    public Description fetch(CytomineConnection connection, String domainName, String domainIdent) throws CytomineException {
        this.set("domainIdent", domainIdent);
        this.set("domainClassName", domainName);
        JSONObject json = connection.doGet(this.toURL());
        this.setAttr(json);
        return this;
    }

    @Override
    public String getJSONResourceURL() {
        set("id", null);
        return super.getJSONResourceURL();
    }
}