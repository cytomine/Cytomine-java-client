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
import be.cytomine.client.models.AttachedFile;
import be.cytomine.client.models.Model;


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

    public static AttachedFileCollection fetchByAssociatedDomain(Model domain) throws CytomineException {
        return fetchByAssociatedDomain(domain,0,0);
    }
    public static AttachedFileCollection fetchByAssociatedDomain(Model domain, int offset, int max) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain,offset, max);
    }
    public static AttachedFileCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain) throws CytomineException {
        return fetchByAssociatedDomain(Cytomine.getInstance().getDefaultCytomineConnection(), domain,0,0);
    }
    public static AttachedFileCollection fetchByAssociatedDomain(CytomineConnection connection, Model domain, int offset, int max) throws CytomineException {
        AttachedFileCollection afc = new AttachedFileCollection(max, offset);
        afc.addFilter("domainClassName", Cytomine.convertDomainName(domain.getClass().getSimpleName().toLowerCase()));
        afc.addFilter("domainIdent", domain.getId().toString());
        return (AttachedFileCollection)afc.fetch();
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
}
