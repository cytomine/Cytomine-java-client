package be.cytomine.client.sample;
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

import be.cytomine.client.Cytomine;
import be.cytomine.client.models.SoftwareUserRepository;
import org.apache.log4j.Logger;

public class SoftwareExample {

    private static final Logger log = Logger.getLogger(SoftwareExample.class);
    public static void addSampleSoftwareUserRepository(Cytomine cytomine) throws Exception {
        SoftwareUserRepository sur = new SoftwareUserRepository("github", "geektortoise", "S_", "cytomineuliege");
        sur.save();
    }

}