package be.cytomine.client.sample;
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

import be.cytomine.client.Cytomine;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class CropExample {

    private static final Logger log = Logger.getLogger(CropExample.class);


    public static void indexAnnotation(Cytomine cytomine) throws Exception {

        cytomine.indexToRetrieval(141221746l, 101903411l, "http://beta.cytomine.be/api/userannotation/141221746/crop.jpg");

    }

    public static void downloadAllAnnotations(Cytomine cytomine) throws Exception {
        //1. check if image == null

        // cytomine.getProject(57L);
/**
 * COPY (SELECT a.id, st_area(a.location) FROM user_annotation a order by st_area(a.location) DESC) TO '/tmp/dump.csv' WITH CSV HEADER
 */

        String format = "png";

        String outPutDir = "/media/DATA_/backup/retrieval/cropClient/";
        BufferedReader br = new BufferedReader(new FileReader("/home/lrollus/temp/dumpB.csv"));
        List<Long> ids = new ArrayList<Long>();
        try {
            String line = br.readLine();
            while (line != null) {
                String id = line.split(",")[0];
                if (!id.equals("id")) {
                    //otherwise, first line
                    ids.add(Long.parseLong(id));
                }

                line = br.readLine();
            }
        } finally {
            br.close();
        }

        List<Long> toIgnore = new ArrayList<Long>();

        System.out.println("There are " + ids.size() + " annotations");
        int testNumber = 0;

        for (int i = 0; i < ids.size(); i++) {
            System.out.println(i + "/" + ids.size() + " ====> TO IGNORE " + toIgnore.size());
            Long id = ids.get(i);
            String outputFile = outPutDir + "" + id + ".png";
            System.out.println(outputFile);
            File file = new File(outputFile);
            if (!file.exists()) {
                try {
                    System.out.println("Try downloading...");
                    cytomine.downloadPictureWithRedirect("http://beta.cytomine.be/api/annotation/" + id + "/crop." + format + "?maxSize=256", outputFile, format);
                    testNumber = 0;
                } catch (Exception e) {
                    testNumber++;
                    //Thread.sleep(10000);
                    if (testNumber < 1) {
                        i--;
                    } else {
                        testNumber = 0;
                        toIgnore.add(id);
                    }
                }
            }
        }
    }
}
