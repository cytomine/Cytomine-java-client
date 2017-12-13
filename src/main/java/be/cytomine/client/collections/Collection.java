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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class Collection<T extends Model> {

    JSONArray list = new JSONArray();
    HashMap<String, String> map = new HashMap<String, String>();
    HashMap<String, String> params = new HashMap<String, String>();

    protected int max;
    protected int offset;
    private Class<T> type;
    private T modelInstance;

    protected Collection(Class<T> type) {
        this(type, 0,0);
    }
    protected Collection(Class<T> type, int max, int offset) {
        this.max = max;
        this.offset = offset;
        this.type = type;
        try {
            modelInstance = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Model> Collection<T> fetch(Class<T> clazz) throws CytomineException {
        return fetch(clazz,0,0);
    }
    public static <T extends Model> Collection<T> fetch(Class<T> clazz, int offset, int max) throws CytomineException {
        Collection<T> c = new Collection<>(clazz, max, offset);
        return Cytomine.getInstance().fetchCollection(c);
    }
    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(Class<T> clazz, Class<U> filter, Long idFilter) throws CytomineException {
        return fetchWithFilter(clazz, filter, idFilter, 0,0);
    }
    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(Class<T> clazz, Class<U> filter, Long idFilter, int offset, int max) throws CytomineException {
        Collection<T> c = new Collection<>(clazz, max, offset);
        try {
            c.addFilter(filter.newInstance().getDomainName(), idFilter.toString());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Cytomine.getInstance().fetchCollection(c);
    }
    public Collection<T> fetchNextPage() throws CytomineException {
        this.offset = this.offset + max;
        return Cytomine.getInstance().fetchCollection(this);
    }

    public T get(int i) {
        modelInstance.setAttr((JSONObject) list.get(i));
        return modelInstance;
    }


    public String toURL() throws CytomineException {
        String url = getJSONResourceURL()+"?";
        for (Map.Entry<String, String> param : params.entrySet()) {
            url += param.getKey() + "=" + param.getValue() + "&";
        }
        url = url.substring(0, url.length() - 1);
        url += getPaginatorURLParams();
        return url;
    }

    public String getDomainName() throws CytomineException {
        if(type == null) throw new CytomineException(400,"Collection not typed. Not possible to get URL.");
        return type.getSimpleName().toLowerCase();
    }

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

    protected String getJSONResourceURL() throws CytomineException {
        if(map.size() > 1) {
            throw new CytomineException(400, "Only one filter is allowed by default");
        }
        final StringBuilder urlB = new StringBuilder("/api");
        map.forEach((k, v) -> {
            urlB.append("/"+k+"/"+v);
        });
        urlB.append("/"+ getDomainName() + ".json");

        return urlB.toString();
    }

    private String getPaginatorURLParams() {
        return "&max=" + this.max + "&offset=" + this.offset;
    }

    public String toString() {
        try {
            return getDomainName() + " collection";
        } catch (CytomineException e) {
            return "collection";
        }
    }
}
