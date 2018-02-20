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

import java.util.List;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class Annotation extends Model<Annotation> {
    public Annotation(){}
    public Annotation(String locationWKT, Long image){
        this.set("location", locationWKT);
        this.set("image", image);
    }
    public Annotation(String locationWKT, Long image, List<Long> terms){
        this(locationWKT, image);
        this.set("term", terms);
    }
    public Annotation(String locationWKT, Long image, Long project){
        this(locationWKT, image);
        this.set("project", project);
    }
}
