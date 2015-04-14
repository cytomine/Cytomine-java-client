package be.cytomine.client.sample;
/*
 * Copyright (c) 2009-2015. Authors: see NOTICE file.
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
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.AbstractImage;
import be.cytomine.client.models.ImageGroup;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.ImageSequence;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ImageExample {

    private static final Logger log = Logger.getLogger(ImageExample.class);

    public static void changeImageName(Cytomine cytomine, Long idImageInstance, String newName) throws Exception{

        System.out.println("Look for image instance " + idImageInstance);
        ImageInstance ii = cytomine.getImageInstance(idImageInstance);

        System.out.println("Look for abstract image " + ii.getLong("baseImage"));
        AbstractImage ai = cytomine.editAbstractImage(ii.getLong("baseImage"),newName);

        System.out.println("Edit name");
        cytomine.editAbstractImage(ai.getId(),newName);

        System.out.println("New name is " + cytomine.getAbstractImage(ai.getId()).getStr("originalFilename"));


    }

    public static void testAddSequence(Cytomine cytomine) throws Exception {

        ImageGroup imageGroup = cytomine.addImageGroup(20475571l);

        ImageSequence imageSequence1 = cytomine.addImageSequence(imageGroup.getId(),84251653l,0,0,0,0);
        ImageSequence imageSequence2 = cytomine.addImageSequence(imageGroup.getId(),84215226l,0,0,0,1);
        ImageSequence imageSequence3 = cytomine.addImageSequence(imageGroup.getId(),84225416l,0,0,0,2);
        ImageSequence imageSequence4 = cytomine.addImageSequence(imageGroup.getId(),84262543l,0,0,0,3);
    }

    public static void testUpload(Cytomine cytomine) throws Exception {
        try {
            //http://shareview.ecampus.ulg.ac.be 9070db9d-cb16-4c8f-a443-ffa4f452db64 dff7c034-9fcb-4615-bf3f-5b3d11b5869e
//             Cytomine cytomine2 = new Cytomine("http://shareview.ecampus.ulg.ac.be", "9070db9d-cb16-4c8f-a443-ffa4f452db64", "dff7c034-9fcb-4615-bf3f-5b3d11b5869e", "./");
//
//             System.out.println(cytomine2.getProjects());
//             User user = cytomine2.getKeys("05b0dbca-5821-44e6-b7f5-4e9ca3669cf6");
//             System.out.println(user);
//             User retrieveUser = cytomine2.getUser("05b0dbca-5821-44e6-b7f5-4e9ca3669cf6");
//             System.out.println(retrieveUser.getId());
//             if (true) return;

            System.out.println(Cytomine.UploadStatus.DEPLOYED);

            // String file="/home/lrollus/Cytomine/dotslide/Images/Image_63/Image63.tif";
            String file="/media/DATA/image/P21-10GH050246-A7_CD3_201404021522.tif";

//             //AUTOINTERSECT.png
            Long idProject = 92279388l;//21919089l;   // and storage 17763541
            Long idStorage = 17763541l;
            String cytomineHost = "http://beta.cytomine.be";//"http://localhost:8080";

//             //elearn
//             Long idProject = 8283911l;
//             Long idStorage = 8266491l;
//             String cytomineHost = "http://elearn.cytomine.be";

            //current
//             Long idProject = 9258788l;
//             Long idStorage = 17763555l;
//             String cytomineHost = "http://current.cytomine.be";

            //beta
//             Long idProject = 21919089l;
//             Long idStorage = 17763555l;
//             String cytomineHost = "http://beta.cytomine.be";

//             Long idProject = 21919089l;
//             Long idStorage = 17763555l;
//             String cytomineHost = "http://beta.cytomine.be";

//             Long idProject = 8578937l;
//             Long idStorage = 8578897l;
////             //String cytomineHost = "http://shareview.ecampus.ulg.ac.be";
//             String cytomineHost = "http://139.165.56.82";
            //beta
//             String cytomineHost = "http://beta.cytomine.be";

            System.out.println("Connection on " + cytomine.host);

            int i=0;

//             Map<String,String> properties = new HashMap<String,String>();
//             properties.put("patient_id","PATIENT"+i);
//             properties.put("sample_id","SAMPLE"+i);
//             properties.put("image_type","IMAGETYPE"+i);
//             properties.put("version","VERSION"+i);

            JSONArray json = cytomine.uploadImage(file,idProject,idStorage,cytomineHost,null,true);
            System.out.println(json.get(0));
//             System.out.println(((JSONObject)json.get(0)).get("uploadFile"));
            System.out.println(((JSONObject)json.get(0)).get("images"));
//
//             String uploadedFilename = (String)((JSONObject)((JSONObject)json.get(0)).get("uploadFile")).get("filename");
//             Long idImage = 0l;
//
//             do {
//                 ImageInstanceCollection collection = cytomine.getImageInstances(idProject);
//                 for(int i=0;i<collection.size();i++) {
//                     String[] expected = uploadedFilename.split("/");
//                     String[] current =  collection.get(i).getStr("filename").split("/");
//                      System.out.println("expected="+expected);
//                     System.out.println("current="+current);
//                     if(expected[0].equals(current[0]) && expected[1].equals(current[1])) {
//                         System.out.println("OK!");
//                         idImage = collection.get(i).getId();
//                     }
//                 }
//                 Thread.sleep(10000);
//             } while(idImage==0);

        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

}
