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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AttachedFile extends Model<AttachedFile> {

    public AttachedFile() {}
    public AttachedFile(Model model, File file) {
        this(model, file.getAbsolutePath());
    }
    public AttachedFile(Model model, String file) {
        this(Cytomine.convertDomainName(model.getClass().getSimpleName().toLowerCase()), model.getId(), file);
    }

    public AttachedFile(String domainClassName, Long idDomain, String file) {
        set("domainIdent", idDomain.toString());
        set("domainClassName", domainClassName);
        set("file", file);
    }

    @Override
    public AttachedFile save() throws CytomineException {
        return this.save(Cytomine.getInstance().getDefaultCytomineConnection());
    }
    @Override
    public AttachedFile save(CytomineConnection connection) throws CytomineException {
        if(getStr("file") == null || getStr("domainIdent") == null || getStr("domainClassName") == null) {
            throw new CytomineException(400, "domainClassName, domainIdent and file attribute must be set");
        }

        Map<String, String> entities = new HashMap<>();
        entities.put("domainIdent",getStr("domainIdent"));
        entities.put("domainClassName",getStr("domainClassName"));
        if(getStr("filename")!=null) entities.put("filename",getStr("filename"));

        JSONObject json = connection.uploadFile(this.toURL(), new File(getStr("file")), entities);
        this.setAttr(json);

        // TODO reactive this when normalization into core
        //this.setAttr((JSONObject) json.get(this.getDomainName()));
        return this;
    }
}
