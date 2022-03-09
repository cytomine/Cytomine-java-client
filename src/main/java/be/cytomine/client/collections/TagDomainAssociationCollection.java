package be.cytomine.client.collections;

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
import be.cytomine.client.models.Model;
import be.cytomine.client.models.TagDomainAssociation;

public class TagDomainAssociationCollection extends Collection {

    public TagDomainAssociationCollection() {
        this(0,0);
    }
    public TagDomainAssociationCollection(int offset, int max) {
        super(TagDomainAssociation.class, max, offset);
    }


    public static TagDomainAssociationCollection fetchByAssociatedDomain(Model domain) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain, 0,0);
    }
    public static TagDomainAssociationCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain) throws CytomineException {
        return fetchByAssociatedDomain(connection, domain, 0,0);
    }

    public static TagDomainAssociationCollection fetchByAssociatedDomain(Model domain, int offset, int max) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain, offset,max);
    }

    public static TagDomainAssociationCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain, int offset, int max) throws CytomineException {
        return (TagDomainAssociationCollection) new TagDomainAssociationCollection(max, offset).fetchWithFilter(connection, domain.getClass(), domain.getId(), offset, max);
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {

        //TODO refactor when normalize URLs
        final StringBuilder prefix = new StringBuilder("");
        String base = "/api/";
        base += "domain/";
        getFilters().forEach((k,v) -> prefix.append(Cytomine.convertDomainName(k.toString())+"/"+v+"/"));
        base += prefix.toString();
        base += "tag_domain_association";//getDomainName();
        base += ".json";

        return base;

    }
}
