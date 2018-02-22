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
import be.cytomine.client.CytomineException;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 * A generic Model
 */
public abstract class Model<T extends Model> {
    /**
     * Attribute for the current model
     */
    JSONObject attr = new JSONObject();

    /**
     * Params map (url params: ?param1=value&...
     */
    HashMap<String, String> params = new HashMap<>();

    /**
     * Filter maps (url params: /api/param1/value/model.json
     */
    LinkedHashMap<String, String> filters = new LinkedHashMap<>();

    //public Model() {}

    /**
     * Build model REST url
     *
     * @return
     */
    public String toURL() {
        return getJSONResourceURL();
    }

    /**
     * Direct method to accessto the id
     *
     * @return Model id
     */
    public Long getId() {
        return getLong("id");
    }

    /**
     * Generate JSON from Model
     *
     * @return JSON
     */
    public String toJSON() {
        return attr.toString();
    }

    /**
     * Get Model URL
     *
     * @return URL
     */
    public String getJSONResourceURL() {
        Long id = getId();
        String base = "/api/";
        base += getFilterPrefix();
        base += getDomainName();
        if(id!= null) {
            base += "/" + id + ".json?";
        } else {
            base += ".json?";
        }

        for (Map.Entry<String, String> param : params.entrySet()) {
            base = base + param.getKey() + "=" + param.getValue() + "&";
        }
        base = base.substring(0, base.length() - 1);
        return base;
    }

    /**
     * Get the name of the domain for the domain
     *
     * @return Domain name
     */
    public String getDomainName(){
        return getClass().getSimpleName().toLowerCase();
    }

    protected String getFilterPrefix() {
        final StringBuilder prefix = new StringBuilder("");
        filters.forEach((k,v) -> prefix.append(k+"/"+v+"/"));
        return prefix.toString();//throw new RuntimeException("Error of the java client. "+getClass().getName()+" does not support filters");
    }

    public T fetch(Long id) throws CytomineException {
        this.set("id", id);
        return Cytomine.getInstance().fetchModel((T)this);
    }

    public T save() throws CytomineException {
        return Cytomine.getInstance().saveModel((T)this);
    }

    public void delete() throws CytomineException {
        this.delete((T) this);
    }
    public void delete(Long id) throws CytomineException {
        this.set("id", id);
        this.delete((T) this);
    }
    private void delete(T model) throws CytomineException {
        Cytomine.getInstance().deleteModel(model);
    }

    public T update() throws CytomineException {
        return Cytomine.getInstance().updateModel((T) this);
    }

    public T update(HashMap<String, Object> attributes) throws CytomineException {
        attributes.forEach((k, v) -> {
            this.set(k,v);
        });
        return Cytomine.getInstance().updateModel((T)this);
    }

    public void addParams(String name, String value) {
        params.put(name, value);
    }

    public JSONObject getAttr() {
        return attr;
    }

    public void setAttr(JSONObject attr) {
        this.attr = attr;
    }

    /**
     * Add value for attribute 'name'
     *
     * @param name  attribute name
     * @param value value for this attribute
     */
    public void set(String name, Object value) {
        attr.put(name, value);
    }

    /**
     * Get value for attribute 'name'
     *
     * @param name attribute name
     * @return value value for this attribute
     */
    public Object get(String name) {
        try {
            return attr.get(name);
        } catch (Exception e) {
            return null;
        }
    }

    public String getStr(String name) {
        if (get(name) == null) {
            return null;
        }
        else {
            return get(name) + "";
        }
    }

    public Integer getInt(String name) {
        String str = getStr(name);
        if (str == null) {
            return null;
        }
        else {
            return Integer.parseInt(str);
        }
    }

    public Long getLong(String name) {
        String str = getStr(name);
        if (str == null) {
            return null;
        }
        else {
            return Long.parseLong(str);
        }
    }

    public Double getDbl(String name) {
        if(get(name) == null) return null;
        if (get(name).getClass().getName().equals("java.lang.Long")) {
            return ((Long) get(name)).doubleValue();
        } else {
            return (Double) get(name);
        }
    }

    public Boolean getBool(String name) {
        return (Boolean) get(name);
    }

    public List getList(String name) {
        return (List) get(name);
    }

    boolean isFilterBy(String name) {
        return filters.containsKey(name);
    }

    public String getFilter(String name) {
        return filters.get(name);
    }

    public /*protected */void addFilter(String name, String value) {
        if(value != null) filters.put(name, value);
    }

    public String toString() {
        return getDomainName() + getId();
    }

    @Override
    public int hashCode() {
        if(getId()!=null) {
            return getId().intValue() ;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Model)) {
            return false;
        } else {
            return ((Model) o).getId().equals(this.getId());
        }
    }
}
