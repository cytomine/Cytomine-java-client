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

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class ReviewedAnnotation extends Model<ReviewedAnnotation> {
    public ReviewedAnnotation() {}
    public ReviewedAnnotation(String locationWKT, ImageInstance image, Project project, ArrayList<Term> terms, Long userId, Long reviewUserId, Annotation parent) {
        this(locationWKT, image.getId(), project.getId(), (ArrayList) terms.stream().map(Model::getId).collect(Collectors.toList()), userId,reviewUserId, parent.getId(), parent.getClass().getName());
    }
    public ReviewedAnnotation(String locationWKT, Long imageId, Long projectId, ArrayList<Long> terms, Long userId, Long reviewUserId, Long parentId, String parentClass) {
        this.set("project", projectId);
        this.set("image", imageId);
        this.set("location", locationWKT);
        this.set("user", userId);
        this.set("reviewUser", reviewUserId);
        this.set("terms", terms);
        this.set("parentIdent",parentId);
        this.set("parentClassName",parentClass);
    }
}
