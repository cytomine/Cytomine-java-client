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
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.PropertyCollection;
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;

public class MetadataExample {

    private static final Logger log = Logger.getLogger(MetadataExample.class);

    public static void testUploadFile(Cytomine cytomine) throws Exception {
        AttachedFile attachedFile = cytomine.uploadAttachedFile("/home/lrollus/Images/test01.jpg", "be.cytomine.project.Project", 21919089L);
        System.out.println(attachedFile);
    }

    public static void testDescription(Cytomine cytomine) throws Exception {
        try {
            log.info("Description :");
            Project project = cytomine.getProject(57L);

            cytomine.addDescription(project.getId(), project.getStr("class"), "Add description for prject");

            Description description = cytomine.getDescription(project.getId(), project.getStr("class"));

            System.out.println("DESCRIPTION=" + description.get("data"));

            cytomine.editDescription(project.getId(), project.getStr("class"), "Add description for project");

            description = cytomine.getDescription(project.getId(), project.getStr("class"));
            System.out.println("DESCRIPTION=" + description.get("data"));

            cytomine.deleteDescription(project.getId(), project.getStr("class"));

        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public static void TestProperty(Cytomine cytomine) throws Exception {

        try {
            //PROPERTY WITH ANNOTATION
            log.info("get annotation = 17774487 :");
            Annotation ua = cytomine.getAnnotation(17774487L);
            log.info("UserAnnotation => " + ua.getAttr());

            log.info("get properties for annotation = 17774487 :");
            PropertyCollection annotationProperties = cytomine.getDomainProperties("annotation", 17774487L);
            log.info("properties for annotation = 17774487 => " + annotationProperties.getList());

            log.info("get property = 17775320 for annotation = 17774487 :");
            Property annotationProperty = cytomine.getDomainProperty(17775320l, 17774487L, "annotation");
            log.info("property = 17775320 for annotation = 17774487 => " + annotationProperty.getAttr());

            log.info("get property for annotation = 17774487 with key = Description :");
            //annotationProperties = cytomine.getPropertyByDomainAndKey("annotation", 17774487L, "Description");
            log.info("property for annotation = 17774487 with key = Description => " + annotationProperties.getList());

            log.info("Create property for annotation :");
            annotationProperty = cytomine.addDomainProperties("annotation", 17774487L, "TestNewKeyClientJava", "TestNewValueClientJava");
            log.info("property for annotation created => " + annotationProperty.getAttr());

            log.info("Edit property for annotation :");
            annotationProperty = cytomine.editDomainProperty("annotation", annotationProperty.getId(), 17774487L, "TestNewKeyClientJava2", "TestNewValueClientJava2");
            log.info("property for annotation edited => " + annotationProperty.getAttr());

            log.info("Delete property for annotation :");
            cytomine.deleteDomainProperty("annotation", annotationProperty.getId(), 17774487L);
            log.info("property for annotation deleted => " + annotationProperty.getAttr());

            log.info("get property key for annotation with project = 14421577 :");
            annotationProperties = cytomine.getKeysForDomain("annotation", "idProject", 14421577L);
            log.info("properties keys for annotation with project = 14421577 => " + annotationProperties.getList());

            log.info("get property key for annotation with image = 14421592 :");
            annotationProperties = cytomine.getKeysForDomain("annotation", "idImage", 14421592L);
            log.info("properties keys for annotation with image = 14421592 => " + annotationProperties.getList());

            //PROPERTY WITH PROJECT
            log.info("get project = 16623 :");
            Project project = cytomine.getProject(16623L);
            log.info("project => " + project.getAttr());

            log.info("get properties for project = 16623 :");
            PropertyCollection projectProperties = cytomine.getDomainProperties("project", 16623L);
            log.info("properties for project = 16623 => " + projectProperties.getList());

            log.info("get property = 17775218 for project = 16623 :");
            Property projectProperty = cytomine.getDomainProperty(17775218L, 16623L, "project");
            log.info("property = 17775218 for project = 16623 => " + projectProperty.getAttr());

            log.info("get property for project = 16623 with key = Ok :");
            //  projectProperties = cytomine.getPropertyByDomainAndKey("project", 16623L, "Ok");
            log.info("property for project = 16623 with key = Ok => " + projectProperties.getList());

            log.info("Create property for project :");
            projectProperty = cytomine.addDomainProperties("project", 16623L, "TestNewKeyClientJava", "TestNewValueClientJava");
            log.info("property for project created => " + projectProperty.getAttr());

            log.info("Edit property for project :");
            projectProperty = cytomine.editDomainProperty("project", projectProperty.getId(), 16623L, "TestNewKeyClientJava2", "TestNewValueClientJava2");
            log.info("property for project edited => " + projectProperty.getAttr());

            log.info("Delete property for project :");
            cytomine.deleteDomainProperty("project", projectProperty.getId(), 16623L);
            log.info("property for project deleted => " + projectProperty.getAttr());


            //PROPERTY WITH IMAGEINSTANCE
            log.info("get imageinstance = 786011 :");
            ImageInstance ii = cytomine.getImageInstance(786011L);
            log.info("ImageInstance => " + ii.getAttr());

            log.info("get properties for imageinstance = 786011 :");
            PropertyCollection imageInstanceProperties = cytomine.getDomainProperties("imageinstance", 786011L);
            log.info("properties for imageinstance = 786011 => " + imageInstanceProperties.getList());

            log.info("get property = 17775242 for imageinstance = 786011 :");
            Property imageInstanceProperty = cytomine.getDomainProperty(17775242L, 786011L, "imageinstance");
            log.info("property = 17775242 for imageinstance = 786011 => " + imageInstanceProperty.getAttr());

            log.info("get property for imageinstance = 786011 with key = Label :");
            //   imageInstanceProperties = cytomine.getPropertyByDomainAndKey("imageinstance", 786011L, "Label");
            log.info("property for imageinstance = 786011 with key = Label => " + imageInstanceProperties.getList());

            log.info("Create property for imageinstance :");
            imageInstanceProperty = cytomine.addDomainProperties("imageinstance", 786011L, "TestNewKeyClientJava", "TestNewValueClientJava");
            log.info("property for imageinstance created => " + imageInstanceProperty.getAttr());

            log.info("Edit property for imageinstance :");
            imageInstanceProperty = cytomine.editDomainProperty("imageinstance", imageInstanceProperty.getId(), 786011L, "TestNewKeyClientJava2", "TestNewValueClientJava2");
            log.info("property for imageinstance edited => " + imageInstanceProperty.getAttr());

            log.info("Delete property for imageinstance :");
            cytomine.deleteDomainProperty("imageinstance", imageInstanceProperty.getId(), 786011L);
            log.info("property for imageinstance deleted => " + imageInstanceProperty.getAttr());

            log.info("get property key for imageinstance with project = 14421577 :");
            imageInstanceProperties = cytomine.getKeysForDomain("imageinstance", "idProject", 75985L);
            log.info("properties keys for imageinstance with project = 14421577 => " + imageInstanceProperties.getList());
        } catch (CytomineException e) {
            log.error(e);
            e.printStackTrace();
        }
    }
}
