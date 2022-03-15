package be.cytomine.client.collections;

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

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnotationCollection extends Collection<Annotation> {

    private static final Logger log = Logger.getLogger(AnnotationCollection.class);

    public AnnotationCollection(int offset, int max) {
        super(Annotation.class, max, offset);
    }

// TODO refactor when rest url are normalized

    public static AnnotationCollection fetchByProject(Project project) throws CytomineException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(project.getClass().getSimpleName().toLowerCase(), project.getId());
        return fetchWithParameters(parameters);
    }
    public static AnnotationCollection fetchByImageInstance(ImageInstance image) throws CytomineException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("image", image.getId());
        return fetchWithParameters(parameters);
    }
    public static AnnotationCollection fetchByUserAndProject(User user, Project project) throws CytomineException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(user.getClass().getSimpleName().toLowerCase(), user.getId());
        parameters.put(project.getClass().getSimpleName().toLowerCase(), project.getId());
        return fetchWithParameters(parameters);
    }
    public static AnnotationCollection fetchByTermAndImageInstance(Term term, ImageInstance image) throws CytomineException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(term.getClass().getSimpleName().toLowerCase(), term.getId());
        parameters.put(Project.class.getSimpleName().toLowerCase(), image.get("project"));
        return fetchWithParameters(parameters);
    }
    public static AnnotationCollection fetchByTermAndProject(Term term, Project project) throws CytomineException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(term.getClass().getSimpleName().toLowerCase(), term.getId());
        parameters.put(project.getClass().getSimpleName().toLowerCase(), project.getId());
        return fetchWithParameters(parameters);
    }

    public static AnnotationCollection fetchWithParameters(Map<String, Object> parameters) throws CytomineException {
        return fetchWithParameters(Cytomine.getInstance().getDefaultCytomineConnection(), parameters, 0, 0);
    }
    public static AnnotationCollection fetchWithParameters(Map<String, Object> parameters, int offset, int max) throws CytomineException {
        return fetchWithParameters(Cytomine.getInstance().getDefaultCytomineConnection(), parameters, offset, max);
    }
    public static AnnotationCollection fetchWithParameters(CytomineConnection connection, Map<String, Object> parameters, int offset, int max) throws CytomineException {
        AnnotationCollection ac = new AnnotationCollection(max, offset);
        for(Map.Entry<String, Object> entry : parameters.entrySet()){
            if(!isValid(entry.getKey())) log.warn("parameter not known. It is possible than it has no effect");
            ac.addFilter(entry.getKey(), entry.getValue().toString());
        }
        return (AnnotationCollection) ac.fetch(connection);
    }

    @Override
    protected String getJSONResourceURL() throws CytomineException {
        return "/api/annotation.json";
    }

    @Override
    protected Map<String, String> getParams() {
        return getFilters();
    }

    private final static ArrayList<String> validParameters = new ArrayList<>();
    static {
        validParameters.add("project");
        validParameters.add("user");
        validParameters.add("term");
        validParameters.add("image");

        validParameters.add("showDefault");
        validParameters.add("showBasic");
        validParameters.add("showMeta");
        validParameters.add("showWKT");
        validParameters.add("showGIS");
        validParameters.add("showTerm");
        validParameters.add("showAlgo");
        validParameters.add("showUser");
        validParameters.add("showImage");

        validParameters.add("hideBasic");
        validParameters.add("hideMeta");
        validParameters.add("hideWKT");
        validParameters.add("hideGIS");
        validParameters.add("hideTerm");
        validParameters.add("hideAlgo");
        validParameters.add("hideUser");
        validParameters.add("hideImage");

        validParameters.add("job");
        validParameters.add("jobForTermAlgo");
        validParameters.add("suggestedTerm");
        validParameters.add("userForTermAlgo");
        validParameters.add("kmeansValue");

        validParameters.add("users");
        validParameters.add("images");
        validParameters.add("terms");

        validParameters.add("notReviewedOnly");
        validParameters.add("noTerm");
        validParameters.add("noAlgoTerm");

        validParameters.add("multipleTerm");
        validParameters.add("kmeans");
        validParameters.add("bbox");
        validParameters.add("bboxAnnotation");
        validParameters.add("baseAnnotation");
        validParameters.add("maxDistanceBaseAnnotation");
        validParameters.add("term");

    }
    private static boolean isValid(String input) {
        return validParameters.contains(input);
    }
}
