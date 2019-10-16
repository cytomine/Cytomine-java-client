package be.cytomine.client.sample;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
public class ImageExample {


    private static final Logger log = Logger.getLogger(ImageExample.class);


    public static void changeImageName(Cytomine cytomine, Long idImageInstance, String newName) throws Exception {

        System.out.println("Look for image instance " + idImageInstance);
        ImageInstance ii = new ImageInstance().fetch(idImageInstance);
        System.out.println("Look for abstract image " + ii.getLong("baseImage"));
        AbstractImage ai = new AbstractImage().fetch(ii.getLong("baseImage"));
        ai.set("originalFilename", newName);
        ai.update();
        System.out.println("New name is " + new AbstractImage().fetch(ai.getId()).getStr("originalFilename"));
    }


    public static void testAddSequence(Cytomine cytomine) throws Exception {

        ImageGroup imageGroup = cytomine.addImageGroup(20475571L);
        ImageSequence imageSequence1 = cytomine.addImageSequence(imageGroup.getId(), 84251653L, 0, 0, 0, 0);
        ImageSequence imageSequence2 = cytomine.addImageSequence(imageGroup.getId(), 84215226L, 0, 0, 0, 1);
        ImageSequence imageSequence3 = cytomine.addImageSequence(imageGroup.getId(), 84225416L, 0, 0, 0, 2);
        ImageSequence imageSequence4 = cytomine.addImageSequence(imageGroup.getId(), 84262543L, 0, 0, 0, 3);

    }

    public static void testUpload(Cytomine cytomine) throws Exception {

        try {
            String file = "/media/DATA/image/P21-10GH050246-A7_CD3_201404021522.tif";
//             //AUTOINTERSECT.png
            Long idProject = 92279388L;//21919089L;   // and storage 17763541
            Long idStorage = 17763541L;
            String cytomineHost = "http://beta.cytomine.be";//"http://localhost:8080";
            System.out.println("Connection on " + cytomine.getHost());
            int i = 0;
            JSONArray json = cytomine.uploadImage(file, idProject, idStorage, cytomineHost, null, true);
            System.out.println(json.get(0));
            System.out.println(((JSONObject) json.get(0)).get("images"));
        } catch (CytomineException e) {
            log.error(e);
        }
    }
}