package be.cytomine.client.collections;

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

import be.cytomine.client.models.Model;
import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public abstract class Collection {

    JSONArray list = new JSONArray();
    HashMap<String, String> map = new HashMap<String, String>();
    HashMap<String, String> params = new HashMap<String, String>();

    protected int max;
    protected int offset;

    public Collection(int max, int offset) {
        this.max = max;
        this.offset = offset;
    }

    public void nextPageIndex() {
        this.offset = this.offset + max;
    }

    public String getPaginatorURLParams() {
        return "&max=" + this.max + "&offset=" + this.offset;
    }

    public abstract String toURL();

    public abstract String getDomainName();

    public JSONArray getList() {
        return list;
    }

    public void setList(JSONArray list) {
        this.list = list;
    }


    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void add(Object o) {
        list.add(((Model) o).getAttr());
    }

    boolean isFilterBy(String name) {
        return map.containsKey(name);
    }

    public String getFilter(String name) {
        return map.get(name);
    }

    public Map<String, String> getFilters() {
        return map;
    }


    public void addFilter(String name, String value) {
        map.put(name, value);
    }

    public void addParams(String name, String value) {
        params.put(name, value);
    }

    public String getJSONResourceURL() {
        if (params.isEmpty()) {
            return "/api/" + getDomainName() + ".json";
        }
        else {
            String base = "/api/" + getDomainName() + ".json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }

    public String getJSONResourceURLWithFilter(String filter1Name) {
        return "/api/" + filter1Name + "/" + getFilter(filter1Name) + "/" + getDomainName() + ".json";
    }

    public String getJSONResourceURLWithFilter(String filter1Name, String filter2Name) {
        return "/api/" + filter1Name + "/" + getFilter(filter1Name) + "/" + filter2Name + "/" + getFilter(filter2Name) + "/" + getDomainName() + ".json";
    }

    public String toString() {
        return getDomainName() + " collection";
    }
}
