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

import be.cytomine.client.CytomineException;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class SoftwareParameter extends Model<SoftwareParameter> {
    public SoftwareParameter(){}
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut, boolean setByServer){
        this.set("name", name);
        this.set("type", type);
        this.set("software", idSoftware);
        this.set("defaultValue", defaultValue);
        this.set("required", required);
        this.set("index", index);
        this.set("uri", uri);
        this.set("uriPrintAttribut", uriPrintAttribut);
        this.set("uriSortAttribut", uriSortAttribut);
        this.set("setByServer", setByServer);
    }
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut){
        this(name, type, idSoftware, defaultValue, required, index, uri, uriSortAttribut, uriPrintAttribut, false);
    }
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index){
        this(name, type, idSoftware, defaultValue, required, index, null, null, null, false);
    }
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index, boolean setByServer){
        this(name, type, idSoftware, defaultValue, required, index, null, null, null, setByServer);
    }


    public SoftwareParameter addSoftwareParameter(String name, String type, Long idSoftware, String defaultValue,
                                                  boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut)
            throws CytomineException {
        return addSoftwareParameter(name, type, idSoftware, defaultValue, required, index, uri, uriSortAttribut,
                uriPrintAttribut, false, false, name, null, null);
    }

    public SoftwareParameter addSoftwareParameter(String name, String type, Long idSoftware, String defaultValue,
                                                  boolean required, int index) throws CytomineException {
        return addSoftwareParameter(name, type, idSoftware, defaultValue, required, index, null, null, null, false,
                false, name, null, null);
    }

    public SoftwareParameter addSoftwareParameter(String name, String type, Long idSoftware, String defaultValue,
                                                  boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut, boolean setByServer, boolean serverParameter,
                                                  String humanName, String valueKey, String commandLineFlag)
            throws CytomineException {
        SoftwareParameter softwareParameter = new SoftwareParameter();
        softwareParameter.set("name", name);
        softwareParameter.set("type", type);
        softwareParameter.set("software", idSoftware);
        softwareParameter.set("defaultValue", defaultValue);
        softwareParameter.set("required", required);
        softwareParameter.set("index", index);
        softwareParameter.set("uri", uri);
        softwareParameter.set("uriPrintAttribut", uriPrintAttribut);
        softwareParameter.set("uriSortAttribut", uriSortAttribut);
        softwareParameter.set("setByServer", setByServer);
        softwareParameter.set("serverParameter", serverParameter);
        softwareParameter.set("humanName", humanName);
        softwareParameter.set("valueKey", valueKey);
        softwareParameter.set("commandLineFlag", commandLineFlag);

        return softwareParameter.save();
    }

}
