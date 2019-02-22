package be.cytomine.client.collections;

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

import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Job;


public class JobCollection extends Collection<Job> {

    public JobCollection() {
        this(0, 0);
    }
    public JobCollection(int offset, int max) {
        super(Job.class, max, offset);
    }

    //TODO remove when rest url are normalized
    protected String getJSONResourceURL() throws CytomineException {
        final StringBuilder urlB = new StringBuilder("/api");
        urlB.append("/"+ getDomainName() + ".json?");
        map.forEach((k, v) -> {
            urlB.append(k + "=" + v + "&");
        });

        urlB.deleteCharAt(urlB.length()-1);

        return urlB.toString();
    }


}
