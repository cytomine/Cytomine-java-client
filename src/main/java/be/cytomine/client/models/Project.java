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
import be.cytomine.client.CytomineException;

public class Project extends Model<Project> {
    public Project(){}
    public Project(String name, Ontology ontology){
        this(name,ontology.getId());
    }
    public Project(String name, Long idOntology){
        this.set("name", name);
        this.set("ontology", idOntology);
    }

    public void addMember(User user) throws CytomineException {
        addMember(user, false);
    }
    public void addMember(User user, boolean admin) throws CytomineException {
        addMember(user.getId(), admin);
    }
    public void addMember(Long userId, boolean admin) throws CytomineException {
        Cytomine.getInstance().getDefaultCytomineConnection().doPost("/api/project/" + this.getId() + "/user/" + userId + (admin ? "/admin" : "") + ".json", "");
    }

    public void removeMember(User user) throws CytomineException {
        removeMember(user, false);
    }
    public void removeMember(User user, boolean admin) throws CytomineException {
        removeMember(user.getId(), admin);
    }
    public void removeMember(Long idUser, boolean admin) throws CytomineException {
        Cytomine.getInstance().getDefaultCytomineConnection().doDelete("/api/project/" + this.getId() + "/user/" + idUser + (admin ? "/admin" : "") + ".json");
    }
}
