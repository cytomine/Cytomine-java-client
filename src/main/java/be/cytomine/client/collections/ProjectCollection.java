package be.cytomine.client.collections;

/*
 * Copyright (c) 2009-2015. Authors: see NOTICE file.
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

import be.cytomine.client.models.Project;
import org.json.simple.JSONObject;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class ProjectCollection extends Collection {

    public ProjectCollection(int offset,int max) {
        super(max,offset);
    }

    public String toURL() {
        if (isFilterBy("user")) {
            return getJSONResourceURLWithFilter("user");
        } else if (isFilterBy("ontology")) {
            return getJSONResourceURLWithFilter("ontology");
        } else {
            return getJSONResourceURL();
        }
    }

    public String getDomainName() { return "project";}

    public Project get(int i) {
        Project project = new Project();
        Object item = list.get(i);
        project.setAttr((JSONObject)item);
        return project;
    }
}
