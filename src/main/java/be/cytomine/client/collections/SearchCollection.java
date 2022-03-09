package be.cytomine.client.collections;

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

public class SearchCollection extends Collection {

    public SearchCollection(int offset, int max) {
        super(null, max, offset);
    }

    public String toURL() {
        return getJSONResourceURLWithFilter("keywords", "operator", "filter");
    }

    public String getDomainName() {
        return "search";
    }

    public String getJSONResourceURLWithFilter(String keywords, String operator, String filter) {
        String url = "/api/" + getDomainName() + ".json?" + keywords + "=" + getFilter(keywords) + "&"
                + operator + "=" + getFilter(operator) + "&"
                + filter + "=" + getFilter(filter);
        if (isFilterBy("projects")) {
            url = url + "&projects=" + getFilter("projects");
        }
        return url;


    }
}
