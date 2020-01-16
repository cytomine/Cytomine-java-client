package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2020. Authors: see NOTICE file.
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

public class ProcessingServer extends Model<ProcessingServer> {
    public ProcessingServer(){}
    public ProcessingServer(String name, String host, String username, Integer port, String type, String processingMethodName){
        this.set("name", name);
        this.set("host", host);
        this.set("username", username);
        this.set("port", port);
        this.set("type", type);
        this.set("processingMethodName", processingMethodName);
    }

    @Override
    public String getDomainName() {
        return "processing_server";
    }

}
