package be.cytomine.client.models;

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

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public abstract class ModelAssociation extends Model {

    public String getJSONResourceURL(String name1, Long id1, String name2, Long id2) {
        return "/api/" + name1 + "/" + id1 + "/" + name2 + "/" + id2 + ".json";
    }

    public String getJSONResourceURL(String name1, Long id1, String name2) {
        return "/api/" + name1 + "/" + id1 + "/" + name2 + ".json";
    }

    public abstract String getEntity1();

    public abstract String getEntity2();

    public String toURL() {
        Long id1 = getLong(getEntity1());
        Long id2 = getLong(getEntity2());
        if (id1 != null && id2 != null) {
            return getJSONResourceURL(getEntity1(), id1, getEntity2(), id2);
        } else if (id1 != null) {
            return getJSONResourceURL(getEntity1(), id1, getEntity2());
        } else if (id2 != null) {
            return getJSONResourceURL(getEntity2(), id2, getEntity1());
        } else {
            return null;
        }
    }
}
