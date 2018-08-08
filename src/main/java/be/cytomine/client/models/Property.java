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

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pierre
 * Date: 23/04/13
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public class Property extends Model<Property> {

    public Property(String domain, Long idDomain){
        this(domain, idDomain, null);
    }
    public Property(Model model){
        this(model.getDomainName(), model.getId());
    }

    public Property(String domain, Long idDomain, String key) {
        this(domain, idDomain, key, null);
    }
    public Property(Model model, String key){
        this(model.getDomainName(), model.getId(), key);
    }

    public Property(String domain, Long idDomain, String key, String value) {
        addFilter(domain,idDomain.toString());
        addFilter("key",key);
        set("value", value);
    }
    public Property(Model model, String key, String value){
        this(model.getDomainName(), model.getId(), key, value);
    }
}
