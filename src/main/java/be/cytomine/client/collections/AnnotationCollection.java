package be.cytomine.client.collections;

/*
 * Copyright (c) 2009-2016. Authors: see NOTICE file.
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
import be.cytomine.client.models.Annotation;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class AnnotationCollection extends Collection<Annotation> {

    public AnnotationCollection(int offset, int max) {
        super(Annotation.class, max, offset);
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {
        String start = "/api/annotation.json?";

        Map<String, String> filters = getFilters();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            start = start + "&" + filter.getKey() + "=" + filter.getValue();
        }

        return start;
    }

}
