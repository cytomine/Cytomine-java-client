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

import java.util.Map;

public class TagDomainAssociation extends Model<TagDomainAssociation> {

    public TagDomainAssociation() {}
    public TagDomainAssociation(Model model) {
        this(model, (Long) null);
    }
    public TagDomainAssociation(Model model, Tag tag) {
        this(model.getClass().getSimpleName().toLowerCase(),model.getId(), tag);
    }
    public TagDomainAssociation(Model model, Long idTag) {
        this(model.getClass().getSimpleName().toLowerCase(),model.getId(), idTag);
    }

    public TagDomainAssociation(String domain, Long idDomain, Tag tag) {
        this(domain, idDomain, tag.getId());
    }
    public TagDomainAssociation(String domain, Long idDomain, Long idTag) {
        addFilter(Cytomine.convertDomainName(domain),idDomain.toString());
        set("tag", idTag);
        set("domainIdent", idDomain);
        set("domainClassName", Cytomine.convertDomainName(domain));
    }

    @Override
    public String getJSONResourceURL() {
        Long id = getId();
        String base = "/api/";
        if(id!= null) {
            base += "tag_domain_association";//getDomainName();
            base += "/" + id + ".json?";
        } else {
            base += "domain/";
            base += getFilterPrefix();
            base += "tag_domain_association";//getDomainName();
            base += ".json?";
        }

        for (Map.Entry<String, String> param : params.entrySet()) {
            base = base + param.getKey() + "=" + param.getValue() + "&";
        }
        base = base.substring(0, base.length() - 1);
        return base;
    }

}
