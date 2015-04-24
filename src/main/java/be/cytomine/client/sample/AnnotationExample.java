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
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.models.Annotation;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class AnnotationExample {
    private static final Logger log = Logger.getLogger(AnnotationExample.class);

    public static void testAddAnnotation(Cytomine cytomine) throws Exception {
        //http://beta.cytomine.be/api/annotation.json?&project=14389966&term=8965510&users=26999622&
        Annotation annotation = cytomine.addAnnotation("POLYGON ((1776 5805, 1577.748830248381 5704.3015775472095, 1355.6796363379806 5692.94500639431, 1148.1901993311983 5772.89394086485, 991.1573278688666 5930.3244838756045, 911.7334320256734 6138.015464328429, 923.6516249363489 6360.0552248897075, 1024.8511427732535 6558.051073790496, 1197.8336695336293 6697.76773002003, 1412.6889548436866 6755.046914430249, 1632.2665679743266 6719.984535671571, 1818.5995448152703 6598.643197563084, 1939.4692208887664 6412.0039198151935, 1973.9761311351722 6192.338329115119, 1916.1537172874469 5977.628601694147, 1776 5805))", 16813l);
        annotation = cytomine.getAnnotation(annotation.getId());
        cytomine.addAnnotationTerm(annotation.getId(), 6443l);    //

    }

    public static void testCopyAnnotation(Cytomine cytomine) throws Exception {
        try {
            Long term = 8965510l;
            Long user = 26999622l;
            Long project = 14389966l;

            Map<String, String> filters = new HashMap<String, String>();
            filters.put("project", project + "");
            filters.put("term", term + "");
            filters.put("showWKT", "true");
            filters.put("showTerm", "true");
            filters.put("showMeta", "true");

            filters.put("user", "16");
            AnnotationCollection annotationsToRemove = cytomine.getAnnotations(filters);
            for (int i = 0; i < annotationsToRemove.size(); i++) {
                cytomine.deleteAnnotation(annotationsToRemove.get(i).getId());
            }

            filters.put("user", user + "");
//            filters.remove("user");
            AnnotationCollection annotations = cytomine.getAnnotations(filters);
            System.out.println(annotations.toURL());
            System.out.println(annotations.size());

            for (int i = 0; i < annotations.size(); i++) {
                System.out.println(i + "/" + annotations.size());
                try {
                    Annotation annotation = annotations.get(i);
                    Annotation annotation2 = cytomine.addAnnotation(annotation.getStr("location"), annotation.getLong("image"));
                    cytomine.addAnnotationTerm(annotation2.getId(), term);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (CytomineException e) {
            log.error(e);
        }
    }


    public static void testListAnnotation(Cytomine cytomine) throws Exception {
        try {
            //http://beta.cytomine.be/api/annotation.json?user=14794107&image=14391346&term=8171841
//            Map<String,String> filters = new HashMap<String,String>();
//            filters.put("user","14794107");
//            filters.put("image","14391346");
//            filters.put("term","8171841");
//
//            AnnotationCollection annotations = cytomine.getAnnotations(filters);
//
//            System.out.println(annotations.toURL());
//            System.out.println(annotations.size());
//             Annotation a = cytomine.getAnnotation(22139158l);
//             a = cytomine.editAnnotation(22139158l,a.getStr("location"));
//            System.out.println(a.getId());

            Annotation a = cytomine.getAnnotation(22170559l);
            System.out.println("======> " + a.getStr("location").split(",").length);
            cytomine.simplifyAnnotation(22170559l, 200l, 400l);
            a = cytomine.getAnnotation(22170559l);
            System.out.println("======> " + a.getStr("location").split(",").length);

        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();
        }
    }


}
