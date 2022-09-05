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
import be.cytomine.client.CytomineException;

import java.util.List;

public class Annotation extends Model<Annotation> {
    public Annotation() {
    }

    public Annotation(String locationWKT, SliceInstance slice) {
        this.set("location", locationWKT);
        this.set("slice", slice.getId());
    }

    public Annotation(String locationWKT, SliceInstance slice, ImageInstance image, Project project) {
        this(locationWKT, slice.getId(), image.getId(), project.getId());
    }

    public Annotation(String locationWKT, Long slice, Long image) {
        this.set("location", locationWKT);
        if(slice != null) this.set("slice", slice);
        if(image != null) this.set("image", image);
    }

    public Annotation(String locationWKT, Long slice, Long image, List<Long> terms) {
        this(locationWKT, slice, image);
        this.set("term", terms);
    }

    public Annotation(String locationWKT, Long slice, Long image, Long project) {
        this(locationWKT, slice, image);
        this.set("project", project);
    }

    //TODO rework when url rest normalized
    public void simplify(Long minPoint, Long maxPoint) throws CytomineException {
        String url = "/api/annotation/" + this.getId() + "/simplify.json?minPoint=" + minPoint + "&maxPoint=" + maxPoint;
        Cytomine.getInstance().getDefaultCytomineConnection().doPut(url, "");
    }
}