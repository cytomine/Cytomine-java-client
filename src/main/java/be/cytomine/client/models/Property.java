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

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pierre
 * Date: 23/04/13
 * Time: 10:09
 * To change this template use File | Settings | File Templates.
 */
public class Property extends Model {

    public String getDomainName() {
        return "property";
    }

    public String toURL() {
        Long id = (Long) get("id");
        Long domainIdent = (Long) get("domainIdent");
        String domain = (String) get("domain");

        if (id != null && domainIdent != null && domain != null) {
            return getJSONResourceURL(id, domainIdent, domain);
        } else if (domainIdent != null && domain != null) {
            return getJSONResourceURL(domainIdent, domain);
        } else {
            return getJSONResourceURL();
        }
    }

    public String getJSONResourceURL(Long id, Long domainIdent, String domain) {
        String domainFix = domain;
        if (domain.contains(".")) {
            domainFix = "domain/" + domain;
        }
        if (params.isEmpty()) {
            return "/api/" + domainFix + "/" + domainIdent + "/property/" + id + ".json";
        } else {
            String base = "/api/" + domainFix + "/" + domainIdent + "/property/" + id + ".json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }


    public String getJSONResourceURL(Long domainIdent, String domain) {
        String domainFix = domain;
        if (domain.contains(".")) {
            domainFix = "domain/" + domain;
        }
        if (params.isEmpty()) {
            return "/api/" + domainFix + "/" + domainIdent + "/property.json";
        } else {
            String base = "/api/" + domainFix + "/" + domainIdent + "/property.json?";
            for (Map.Entry<String, String> param : params.entrySet()) {
                base = base + param.getKey() + "=" + param.getValue() + "&";
            }
            base = base.substring(0, base.length() - 1);
            return base;
        }
    }
}
