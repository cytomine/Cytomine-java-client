package be.cytomine.client.models;

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

public class AbstractImage extends Model<AbstractImage> {

    public AbstractImage(){}
    public AbstractImage(String filename, String mime){
        this.set("filename",filename);
        this.set("path",filename);
        this.set("mime",mime);
    }
    @Override
    public String toURL() {
        Long id = getLong("id");
        if (id != null) {
            return getJSONResourceURL();
        } else if (isFilterBy("uploadedFile")) {
            return "/api/uploadedfile/" + getFilter("uploadedFile") + "/image.json";
        } else {
            return getJSONResourceURL();
        }
    }

    public String clearProperties() throws CytomineException {
        return Cytomine.getInstance().getDefaultCytomineConnection().doPost("/api/abstractimage/" + this.getId() + "/properties/clear.json", "").toString();
    }

    public String populateProperties() throws CytomineException {
        return Cytomine.getInstance().getDefaultCytomineConnection().doPost("/api/abstractimage/" + this.getId() + "/properties/populate.json", "").toString();
    }

    public String extractUsefulProperties() throws CytomineException {
        return Cytomine.getInstance().getDefaultCytomineConnection().doPost("/api/abstractimage/" + this.getId() + "/properties/extract.json", "").toString();
    }

}
