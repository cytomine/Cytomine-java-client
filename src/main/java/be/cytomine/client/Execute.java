package be.cytomine.client;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Execute {

    private static final Logger log = LogManager.getLogger(Execute.class);

    public static void main(String[] args) throws Exception {
        log.info("Connection to cytomine...");

        Cytomine.connection(args[0], args[1], args[2]);

        ping(Cytomine.getInstance());

    }

    public static void ping() throws CytomineException {
        log.info("Hello " + Cytomine.getInstance().getCurrentUser().get("username"));
    }
    public static void ping(Cytomine cytomine) throws CytomineException {
        log.info("Hello " + cytomine.getCurrentUser().get("username"));
    }
}
