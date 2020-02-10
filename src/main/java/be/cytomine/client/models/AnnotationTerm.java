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

import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;

public class AnnotationTerm extends Model<AnnotationTerm> implements ICompositePrimaryKey<AnnotationTerm> {

    public AnnotationTerm() {}
    public AnnotationTerm(Long idAnnotation, Long idTerm, Long idExpectedTerm, Long idUser, double rate) {
        this.set("annotation", idAnnotation);
        this.set("userannotation", idAnnotation);
        this.set("term", idTerm);
        this.set("expectedTerm", idExpectedTerm);
        this.set("user", idUser);
        this.set("rate", rate);
    }
    public AnnotationTerm(Long idAnnotation, Long idTerm) {
        this.set("annotation", idAnnotation);
        this.set("userannotation", idAnnotation);
        this.set("term", idTerm);
    }
    public String getDomainName() {
        return "annotationterm";
    }

    public String getEntity1() {
        return "annotation";
    }

    public String getEntity2() {
        return "term";
    }

    public String toURL() {
        return "/api/annotation/" + getStr("userannotation") + "/term/" + getStr("term") + ".json?annotationIdent=" + getStr("userannotation");
    }

    @Override
    public AnnotationTerm fetch(String idAnnotation, String idTerm) throws CytomineException {
        return null;
    }

    @Override
    public AnnotationTerm fetch(CytomineConnection connection, String idAnnotation, String idTerm) throws CytomineException {
        return null;
    }
}
