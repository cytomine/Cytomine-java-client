package be.cytomine.client.collections;

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

import be.cytomine.client.CytomineException;
import be.cytomine.client.models.ReviewedAnnotation;
import org.json.simple.JSONObject;

public class ReviewedAnnotationCollection extends Collection<ReviewedAnnotation> {

    public ReviewedAnnotationCollection(int offset, int max) {
        super(ReviewedAnnotation.class, max, offset);
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {
        if (isFilterBy("term") && isFilterBy("imageinstance")) {
            return "/api/annotation?reviewed=true&term=" + getFilter("term") + "&image=" + getFilter("imageinstance") + "&showGIS=true&showMeta=true&showTerm=true";
        } else {
            return super.getJSONResourceURL();
        }
    }

}
