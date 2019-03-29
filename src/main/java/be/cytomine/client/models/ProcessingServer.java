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
import be.cytomine.client.collections.Collection;
import be.cytomine.client.collections.ProcessingServerCollection;

/**
 * User: rhoyoux
 * Date: 19/08/15
 * GIGA-ULg
 */
public class ProcessingServer extends Model<ProcessingServer> {
    public ProcessingServer(){}
    public ProcessingServer(String url){
        this.set("url", url);
    }

    @Override
    public String getDomainName() {
        return "processing_server";
    }

    public void deleteProcessingServer(Long id) throws CytomineException {
        ProcessingServer processingServer = new ProcessingServer();
        processingServer.set("id", id);
        delete(processingServer.getId());
    }
}
