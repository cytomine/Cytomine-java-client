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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    //private Class<T> type;
    private T modelInstance;

    protected Collection(Class<T> type) {
        this(type, 0,0);
    }
    public Collection(Class<T> type, int max, int offset) {
        this.max = max;
        this.offset = offset;
        //this.type = type;
        try {
            modelInstance = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // ####################### URL #######################

    public String toURL() throws CytomineException {
        StringBuilder url = new StringBuilder(getJSONResourceURL());

        if (getAllURLParams().size() > 0) {
            url.append("?");
        }

        for (Map.Entry<String, String> param : getAllURLParams().entrySet()) {
            url.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }

        String builtUrl = url.toString();
        if (builtUrl.charAt(url.length() - 1) == '&') {
            builtUrl = builtUrl.substring(0, url.length() - 1);
        }

        return builtUrl;
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

    protected Map<String, String> getParams() {
        return params;
    }

    protected Map<String, String> getAllURLParams() {
        HashMap<String, String> allParams = new HashMap<>();
        allParams.putAll(getParams());
        allParams.putAll(getPaginatorURLParams());
        return allParams;
    }

    private Map<String, String> getPaginatorURLParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("max", String.valueOf(this.max));
        params.put("offset", String.valueOf(this.offset));
        return params;
    }

    public String getDomainName() throws CytomineException {
        if(modelInstance == null) throw new CytomineException(400,"Collection not typed. Not possible to get URL.");
        return modelInstance.getDomainName();
    }

    // ####################### REST METHODS #######################

    public static <T extends Model> Collection<T> fetch(Class<T> clazz) throws CytomineException {
        return fetch(clazz,0,0);
    }
    public static <T extends Model> Collection<T> fetch(CytomineConnection connection, Class<T> clazz) throws CytomineException {
        return fetch(connection, clazz,0,0);
    }
    public static <T extends Model> Collection<T> fetch(Class<T> clazz, int offset, int max) throws CytomineException {
        return fetch(Cytomine.getInstance().getDefaultCytomineConnection(), clazz, offset, max);
    }
    public static <T extends Model> Collection<T> fetch(CytomineConnection connection, Class<T> clazz, int offset, int max) throws CytomineException {
        Collection<T> c = new Collection<>(clazz, max, offset);
        return c.fetch(connection);
    }
    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(Class<T> clazz, Class<U> filter, Long idFilter) throws CytomineException {
        return fetchWithFilter(Cytomine.getInstance().getDefaultCytomineConnection(), clazz, filter, idFilter, 0,0);
    }
    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(CytomineConnection connection, Class<T> clazz, Class<U> filter, Long idFilter) throws CytomineException {
        return fetchWithFilter(connection, clazz, filter, idFilter, 0,0);
    }
    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(Class<T> clazz, Class<U> filter, Long idFilter, int offset, int max) throws CytomineException {
        return fetchWithFilter(Cytomine.getInstance().getDefaultCytomineConnection(), clazz, filter, idFilter, offset, max);
    }

    public static <T extends Model, U extends Model> Collection<T> fetchWithFilter(CytomineConnection connection, Class<T> clazz, Class<U> filter, Long idFilter, int offset, int max) throws CytomineException {
        Collection<T> c = new Collection<>(clazz, max, offset);
        try {
            c.addFilter(filter.newInstance().getDomainName(), idFilter.toString());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return c.fetch(connection,offset,max);
    }
    protected <U extends Model> Collection<T> fetchWithFilter(CytomineConnection connection, Class<U> filter, Long idFilter, int offset, int max) throws CytomineException {
        try {
            this.addFilter(filter.newInstance().getDomainName(), idFilter.toString());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this.fetch(connection,offset,max);
    }


    public Collection<T> fetch() throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection());
    }

    public Collection<T> fetch(CytomineConnection connection) throws CytomineException {
        return this.fetch(connection,this.offset ,this.max );
    }

    public Collection<T> fetch(int offset, int max) throws CytomineException {
        return this.fetch(Cytomine.getInstance().getDefaultCytomineConnection(), offset, max);
    }

    public Collection<T> fetch(CytomineConnection connection, int offset, int max) throws CytomineException {
        this.offset =offset;
        this.max = max;
        JSONObject json = connection.doGet(this.toURL());
        this.setList((JSONArray) json.get("collection"));
        return this;
    }
    public Collection<T> fetchNextPage() throws CytomineException {
        return this.fetchNextPage(Cytomine.getInstance().getDefaultCytomineConnection());
    }
    public Collection<T> fetchNextPage(CytomineConnection connection) throws CytomineException {
        this.offset = this.offset + max;
        return this.fetch(connection);
    }

    // ####################### Getters/Setters #######################

    public T get(int i) {
        modelInstance.setAttr((JSONObject) list.get(i));
        return modelInstance;
    }

    public List<Long> getListIds() {
        List<Long> l = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            l.add(this.get(i).getId());
        }
        return l;
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

    public String toString() {
        try {
            return getDomainName() + " collection ("+size()+" elements)";
        } catch (CytomineException e) {
            return "collection";
        }
    }
}
