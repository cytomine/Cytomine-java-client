package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2019. Authors: see NOTICE file.
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

public class SoftwareParameter extends Model<SoftwareParameter> {
    public SoftwareParameter(){}
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut, boolean setByServer, boolean serverParameter, String humanName, String valueKey, String commandLineFlag){
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
        this.set("serverParameter", serverParameter);
        this.set("humanName", humanName);
        this.set("valueKey", valueKey);
        this.set("commandLineFlag", commandLineFlag);
    }
    public SoftwareParameter(String name, String type, Long idSoftware, String defaultValue, boolean required, int index, String uri, String uriSortAttribut, String uriPrintAttribut, boolean setByServer){
        this(name, type, idSoftware, defaultValue, required, index, uri, uriSortAttribut, uriPrintAttribut, setByServer, false, null, null, null);
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

    @Override
    public String getDomainName() {
        return "software_parameter";
    }
}
