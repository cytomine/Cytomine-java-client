package be.cytomine.client;

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

import be.cytomine.client.collections.*;
import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Cytomine {

    private static final Logger log = Logger.getLogger(Cytomine.class);
    static Cytomine CYTOMINE;

    CytomineConnection defaultCytomineConnection;
    private String host;
    private String login;
    private String pass;
    private String publicKey;
    private String privateKey;
    private String charEncoding = "UTF-8";

    private int max = 0;
    private int offset = 0;

    public enum Filter {ALL, PROJECT, ANNOTATION, IMAGE, ABSTRACTIMAGE} //TODO=> RENAME IMAGE TO IMAGEINSTANCE

    public enum Operator {OR, AND}

    /**
     * Init a Cytomine object
     *
     * @param host       Full url of the Cytomine instance (e.g. 'http://...')
     * @param publicKey  Your cytomine public key
     * @param privateKey Your cytomine private key
     */
    public static synchronized CytomineConnection connection(String host, String publicKey, String privateKey) {
        return connection(host, publicKey, privateKey, true);
    }

    public static synchronized CytomineConnection connection(String host, String publicKey, String privateKey, boolean setAsDefault) {
        if (CYTOMINE == null) CYTOMINE = new Cytomine(host, publicKey, privateKey);
        else {
            CYTOMINE.host = host;
            CYTOMINE.publicKey = publicKey;
            CYTOMINE.privateKey = privateKey;
        }
        CytomineConnection connection = new CytomineConnection(host, publicKey, privateKey);
        if (setAsDefault) {
            CYTOMINE.defaultCytomineConnection = connection;
        }
        return connection;
    }

    public CytomineConnection getDefaultCytomineConnection() {
        return defaultCytomineConnection;
    }

    /**
     * Get Cytomine singleton
     */
    public static Cytomine getInstance() throws CytomineException {
        if (CYTOMINE == null) throw new CytomineException(400, "Connection parameters not set");
        return CYTOMINE;
    }

    private Cytomine(String host, String publicKey, String privateKey) {
        this.host = host;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.login = publicKey;
        this.pass = privateKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCharEncoding() {
        return charEncoding;
    }

    public void setCharEncoding(String charEncoding) {
        this.charEncoding = charEncoding;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Get the public key of this connection.
     *
     * @return
     * @author Philipp Kainz
     * @since
     */
    public String getPublicKey() {
        return this.publicKey;
    }

    /**
     * Get the private key of this connection.
     *
     * @return
     * @author Philipp Kainz
     * @since
     */
    public String getPrivateKey() {
        return this.privateKey;
    }

    /**
     * Test the connection to the Cytomine host instance.
     * This test can be run by external applications to check for the
     * availability of the Cytomine-Core.
     *
     * @return true, if HTTP response code (200, 201, 304), i.e. the host is available
     * @throws Exception if the host is not available
     */
    boolean testHostConnection() throws Exception {
        HttpClient client = new HttpClient();
        client.connect(getHost() + "/server/ping", login, pass);
        int code = 0;
        try {
            code = client.get();
        } catch (Exception e) {
            log.error(e.toString());
        }
        client.disconnect();
        return code == 200 || code == 201 || code == 304;
    }

    /**
     * Go to the next page of a collection
     *
     * @param collection Collection
     * @return False if end of collection (previous was last page)
     * @throws Exception
     */
    public boolean nextPage(Collection collection) throws CytomineException {
        collection.fetchNextPage();
        return !collection.isEmpty();
    }

    private String buildEncode(String keywords) throws CytomineException {
        try {
            return URLEncoder.encode(keywords, getCharEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new CytomineException(e);
        }
    }

    private void analyzeCode(int code, JSONObject json) throws CytomineException {

        //if 200,201,...no exception
        if (code >= 400 && code < 600) {
            throw new CytomineException(code, json);
        } else if (code == 302) {
            throw new CytomineException(code, json);
        }
    }

    private JSONObject createJSONResponse(int code, String response) throws CytomineException {
        try {
            Object obj = JSONValue.parse(response);
            return (JSONObject) obj;
        } catch (Exception e) {
            log.error(e);
            throw new CytomineException(code, response);
        } catch (Error e) {
            log.error(e);
            throw new CytomineException(code, response);
        }
    }

    private JSONArray createJSONArrayResponse(int code, String response) throws CytomineException {
        Object obj = JSONValue.parse(response);
        return (JSONArray) obj;
    }



    public String clearAbstractImageProperties(Long idImage) throws CytomineException {
        AbstractImage abs = new AbstractImage();
        abs.set("id", idImage);
        return abs.clearProperties();
    }

    public String populateAbstractImageProperties(Long idImage) throws CytomineException {
        AbstractImage abs = new AbstractImage();
        abs.set("id", idImage);
        return abs.populateProperties();
    }

    public String extractUsefulAbstractImageProperties(Long idImage) throws CytomineException {
        AbstractImage abs = new AbstractImage();
        abs.set("id", idImage);
        return abs.extractUsefulProperties();
    }

    public void downloadAbstractImage(long ID, int maxSize, String dest) throws CytomineException {
        String url = getHost() + "/api/abstractimage/" + ID + "/thumb.png?maxSize=" + maxSize;
        getDefaultCytomineConnection().downloadPicture(url, dest, "png");
    }

    public void downloadImageInstance(long ID, String dest) throws CytomineException {
        getDefaultCytomineConnection().downloadFile("/api/imageinstance/" + ID + "/download", dest);
    }

    public BufferedImage getAbstractImageThumb(long ID, int maxSize) throws CytomineException {
        String url = getHost() + "/api/abstractimage/" + ID + "/thumb.png?maxSize=" + maxSize;
        return getDefaultCytomineConnection().getPictureAsBufferedImage(url, "png");
    }

    public AttachedFile uploadAttachedFile(String file, String domainClassName, Long domainIdent) throws CytomineException {
        String url = "/api/attachedfile.json?domainClassName=" + domainClassName + "&domainIdent=" + domainIdent;
        JSONObject json = getDefaultCytomineConnection().uploadFile(url, file);
        AttachedFile attachedFile = new AttachedFile();
        attachedFile.setAttr(json);
        return attachedFile;
    }

    public void uploadJobData(Long idJobData, byte[] data) throws CytomineException {
        JobData jobData = new JobData();
        jobData.set("id",idJobData);
        jobData.uploadJobData(data);
    }

    public void downloadJobData(Long idJobData, String file) throws CytomineException {
        JobData jobData = new JobData();
        jobData.set("id",idJobData);
        jobData.downloadJobData(file);
    }

    public void downloadAnnotation(Annotation annotation, String path) throws CytomineException {
        String url = annotation.getStr("url");
        getDefaultCytomineConnection().downloadPicture(url, path);
    }


    public void uploadImage(String file, String cytomineHost) throws CytomineException {
        uploadImage(file, null, null, cytomineHost);
    }

    /**
     * Upload and create an abstract image on the plateform (use async upload)
     *
     * @param file         The image file path
     * @param idProject    If not null, add the image in this project
     * @param idStorage    The storage where the image will be copied
     * @param cytomineHost The URL of the Core
     * @return A response with the status, the uploadedFile and the AbstractImage list
     * @throws Exception Error during upload
     */
    public JSONArray uploadImage(String file, Long idProject, Long idStorage, String cytomineHost) throws CytomineException {
        return uploadImage(file, idProject, idStorage, cytomineHost, null, false);
    }

    /**
     * Upload and create an abstract image on the plateform (use async or sync upload depending on synchrone parameter)
     *
     * @param file         The image file path
     * @param idProject    If not null, add the image in this project
     * @param idStorage    The storage where the image will be copied
     * @param cytomineHost The URL of the Core
     * @param properties   These key-value will be add to the AbstractImage as Property domain instance
     * @param synchrone    If true, the response will be send from server when the image will be converted, transfered, created,...(May take a long time)
     *                     Otherwise the server response directly after getting the image and the parameters
     * @return A response with the status, the uploadedFile and the AbstractImage list (only if synchrone!=true)
     * @throws Exception Error during upload
     */
    public JSONArray uploadImage(String file, Long idProject, Long idStorage, String cytomineHost, Map<String, String> properties, boolean synchrone) throws CytomineException {

        //TODO creer une connection vers upload !!!

        Map<String, String> entityParts = new HashMap<>();
        if (idProject != null) {
            entityParts.put("idProject", idProject + "");
        }
        if (idStorage != null) {
            entityParts.put("idStorage", idStorage + "");
        }

        String projectParam = "";
        if (idProject != null && idProject != 0l) {
            projectParam = "&idProject=" + idProject;
        }

        String url = "/upload?idStorage=" + idStorage + "&cytomine=" + cytomineHost + projectParam;
        if (properties != null && properties.size() > 0) {
            List<String> keys = new ArrayList<String>();
            List<String> values = new ArrayList<String>();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                keys.add(entry.getKey());
                values.add(entry.getValue());
            }
            url = url + "&keys=" + StringUtils.join(keys, ",") + "&values=" + StringUtils.join(values, ",");
        }
        if (synchrone) {
            url = url + "&sync=" + true;
        }

        //NON  !!! connection vers upload !!!
        return getDefaultCytomineConnection().uploadImage(file, url, entityParts );
    }




    public void simplifyAnnotation(Long idAnnotation, Long minPoint, Long maxPoint) throws CytomineException {
        String url = "/api/annotation/" + idAnnotation + "/simplify.json?minPoint=" + minPoint + "&maxPoint=" + maxPoint;
        defaultCytomineConnection.doPut(url, "");
    }

    /**
     * Deletes all existing terms assigned by the calling user
     * from an annotation and sets a unique term.
     * <p>
     * This is basically the client implementation of the web-service endpoint
     * <code>/api/annotation/:idAnnotation/term/:idTerm/clearBefore.json</code>.
     * </p>
     *
     * @param idAnnotation
     * @param idTerm
     * @return the AnnotationTerm instance
     * @throws CytomineException if the term cannot be set
     * @author Philipp Kainz
     * @since 2015-01-06
     */
    public AnnotationTerm setAnnotationTerm(Long idAnnotation, Long idTerm) throws CytomineException {
        try {
            AnnotationTerm annotationTerm = new AnnotationTerm();
            annotationTerm.set("userannotation", idAnnotation);
            annotationTerm.set("annotationIdent", idAnnotation);
            annotationTerm.set("term", idTerm);

            // bypass the default saveModel(Model model) method and use the
            // clearBefore url for setting the term to the annotation
            Model model = annotationTerm;

            String clearBeforeURL = "/api/annotation/" + idAnnotation + "/term/"
                    + idTerm + "/clearBefore.json";

            HttpClient client = null;
            client = new HttpClient(publicKey, privateKey, getHost());
            client.authorize("POST", clearBeforeURL, "", "application/json,*/*");
            client.connect(getHost() + clearBeforeURL);
            int code = client.post(model.toJSON());
            String response = client.getResponseData();
            client.disconnect();
            JSONObject json = createJSONResponse(code, response);
            analyzeCode(code, json);
            model.setAttr((JSONObject) json.get(model.getDomainName()));
            return (AnnotationTerm) model;
        } catch (IOException e) {
            throw new CytomineException(e);
        }
    }

    public AnnotationCollection getAnnotationsByTermAndProject(Long idTerm, Long idProject) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("term", idTerm + "");
        annotations.addFilter("project", idProject + "");
        return (AnnotationCollection) annotations.fetch();
    }

    public AnnotationCollection getAnnotations(Map<String, String> filters) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        //http://beta.cytomine.be/api/annotation.json?user=14794107&image=14391346&term=8171841
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            annotations.addFilter(entry.getKey(), entry.getValue());
        }
        return (AnnotationCollection) annotations.fetch();
    }

    public AnnotationCollection getAnnotationsByTermAndImage(Long idTerm, Long idImage) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("term", idTerm + "");
        annotations.addFilter("imageinstance", idImage + "");
        return (AnnotationCollection) annotations.fetch();
    }

    public AnnotationTerm getAnnotationTerm(Long idAnnotation, Long idTerm) throws CytomineException {
        AnnotationTerm annotationTerm = new AnnotationTerm(idAnnotation,idTerm);
        return annotationTerm.fetch(idAnnotation.toString(),idTerm.toString());
    }

    public void deleteAnnotationTerm(Long idAnnotation, Long idTerm) throws CytomineException {
        AnnotationTerm annotationTerm = new AnnotationTerm();
        annotationTerm.set("annotation", idAnnotation);
        annotationTerm.set("userannotation", idAnnotation);
        annotationTerm.set("term", idTerm);
        annotationTerm.delete();
    }

    public void addUserFromLDAP(String username) throws CytomineException {
        defaultCytomineConnection.doPost("/api/ldap/user.json?username=" + username, "");
    }

    public User getCurrentUser() throws CytomineException {
        User user = new User();
        user.set("current", "current");
        return user.fetch(null);
    }

    /*public User getUserByUsername(String username) throws CytomineException {
        User user = new User();
        user.set("username get", username);
        return defaultCytomineConnection.fetchModel(user);
    }*/

    public void addACL(String domainClassName, Long domainIdent, Long idUser, String auth) throws CytomineException {
        defaultCytomineConnection.doPost("/api/domain/" + domainClassName + "/" + domainIdent + "/user/" + idUser + ".json?auth=" + auth, "");
    }

    public void addUserProject(Long idUser, Long idProject) throws CytomineException {
        addUserProject(idUser, idProject, false);
    }

    public void addUserProject(Long idUser, Long idProject, boolean admin) throws CytomineException {
        defaultCytomineConnection.doPost("/api/project/" + idProject + "/user/" + idUser + (admin ? "/admin" : "") + ".json", "");
    }

    public void deleteUserProject(Long idUser, Long idProject) throws CytomineException {
        deleteUserProject(idUser, idProject, false);
    }

    public void deleteUserProject(Long idUser, Long idProject, boolean admin) throws CytomineException {
        defaultCytomineConnection.doDelete("/api/project/" + idProject + "/user/" + idUser + (admin ? "/admin" : "") + ".json");
    }

    public UserCollection getProjectUsers(Long idProject) throws CytomineException {
        UserCollection users = new UserCollection(offset, max);
        users.addFilter("project", idProject + "");
        return (UserCollection) users.fetch();
    }

    public UserCollection getProjectAdmins(Long idProject) throws CytomineException {
        UserCollection users = new UserCollection(offset, max);
        users.addFilter("project", idProject + "");
        users.addFilter("admin", "true");
        return (UserCollection) users.fetch();
    }


    public void doUserPosition(Long idImage, Long x, Long y, Long zoom) throws CytomineException {
        //image, coord.x, coord.y, coord.zoom
        String data = "{image : " + idImage + ", lat : " + x + ", lon : " + y + ", zoom : " + zoom + "}";
        defaultCytomineConnection.doPost("/api/imageinstance/" + idImage + "/position.json", data);
    }

    public void doUserPosition(
            Long idImage,
            Long zoom,
            Long bottomLeftX,
            Long bottomLeftY,
            Long bottomRightX,
            Long bottomRightY,
            Long topLeftX,
            Long topLeftY,
            Long topRightX,
            Long topRightY) throws CytomineException {
        //image, coord.x, coord.y, coord.zoom
        String data = "{image : " + idImage + ", zoom : " + zoom
                + ", bottomLeftX : " + bottomLeftX + ", bottomLeftY : " + bottomLeftY
                + ", bottomRightX : " + bottomRightX + ", bottomRightY : " + bottomRightY
                + ", topLeftX : " + topLeftX + ", topLeftY : " + topLeftY
                + ", topRightX : " + topRightX + ", topRightY : " + topRightY + "}";
        defaultCytomineConnection.doPost("/api/imageinstance/" + idImage + "/position.json", data);
    }

    public User getUser(String publicKey) throws CytomineException {
        User user = new User();
        user.addParams("publicKey", publicKey);
        return user.fetch(null);
    }

    public User getKeys(String publicKey) throws CytomineException {
        User user = new User();
        user.addFilter("publicKeyFilter", publicKey);
        return user.fetch(null);
    }

    public User getKeys(Long id) throws CytomineException {
        User user = new User();
        user.addFilter("id", id + "");
        user.addFilter("keys", "keys");
        return user.fetch(id);
    }

    /*public User getKeysByUsername(String username) throws CytomineException {
        User user = new User();
        user.addFilter("id", username + "");
        user.addFilter("keys", "keys");
        return user.fetch();
    }*/

    public Job changeStatus(Long id, int status, int progress) throws CytomineException {
        return this.changeStatus(id, status, progress, null);
    }

    public Job changeStatus(Long id, int status, int progress, String comment) throws CytomineException {
        Job job = new Job().fetch(id);
        job.set("progress", progress);
        job.set("status", status);
        job.set("statusComment", comment);
        return job.update();
    }

    public User addUserJob(Long idSoftware, Long idProject, Long idUserParent) throws CytomineException {
        return addUserJob(idSoftware, idUserParent, idProject, new Date(), null);
    }

    public User addUserJob(Long idSoftware, Long idUserParent, Long idProject, Date created, Long idJob) throws CytomineException {
        UserJob user = new UserJob();
        user.set("parent", idUserParent);
        user.set("software", idSoftware);
        user.set("project", idProject);
        user.set("job", idJob);
        user.set("created", created.getTime());
        user = user.save();
        User userFinal = new User();
        userFinal.setAttr(user.getAttr());
        return userFinal;
    }

    public void unionAnnotation(Long idImage, Long idUser, Integer minIntersectionLength) throws CytomineException {
        AnnotationUnion annotation = new AnnotationUnion();
        annotation.addParams("idImage", idImage + "");
        annotation.addParams("idUser", idUser + "");
        annotation.addParams("minIntersectionLength", minIntersectionLength + "");
        annotation.update();
    }

    public ReviewedAnnotationCollection getReviewedAnnotationsByTermAndImage(Long idTerm, Long idImage) throws CytomineException {
        ReviewedAnnotationCollection reviewed = new ReviewedAnnotationCollection(offset, max);
        reviewed.addFilter("term", idTerm + "");
        reviewed.addFilter("imageinstance", idImage + "");
        return (ReviewedAnnotationCollection)reviewed.fetch();
    }


    //PROPERTY
    public Property getDomainProperty(Long id, Long domainIdent, String domain) throws CytomineException {
        Property property = new Property(domain,domainIdent);
        return property.fetch(id);
    }

    public PropertyCollection getDomainProperties(String domain, Long domainIdent) throws CytomineException {
        PropertyCollection properties = new PropertyCollection(offset, max);
        properties.addFilter(domain, domainIdent + "");
        properties.addFilter("domainClassName", domain + "");
        properties.addFilter("domainIdent", domainIdent + "");
        return (PropertyCollection)properties.fetch();
    }

    public Property getPropertyByDomainAndKey(String domain, Long domainIdent, String key) throws CytomineException {
        Property property = new Property(domain,domainIdent);
        property.set("key", key);
        return property.fetch(key);
    }

    public Property addDomainProperties(String domain, Long domainIdent, String key, String value) throws CytomineException {
        Property property = new Property(domain,domainIdent);
        property.set("key", key);
        property.set("value", value);
        return property.save();
    }

    public Property editDomainProperty(String domain, Long id, Long domainIdent, String key, String value) throws CytomineException {
        Property property = getDomainProperty(id, domainIdent, domain);
        property.set("domain", domain);
        property.set("domainIdent", domainIdent);
        property.set("key", key);
        property.set("value", value);
        return property.update();
    }

    public void deleteDomainProperty(String domain, Long id, Long domainIdent) throws CytomineException {
        Property property = getDomainProperty(id, domainIdent, domain);
        property.set("id", id);
        property.set("domain", domain);
        property.set("domainIdent", domainIdent);
        property.delete();
    }

    //For ImageInstance ==> idDomain = id of Project
    //and Annotation ==> idDomain = id of Project or Image
    public PropertyCollection getKeysForDomain(String domain, String nameIdDomain, Long idDomain) throws CytomineException {
        PropertyCollection properties = new PropertyCollection(offset, max);
        properties.addFilter(domain, "");
        properties.addFilter("idDomain", idDomain + "");
        properties.addFilter("nameIdDomain", nameIdDomain);
        return (PropertyCollection)properties.fetch();
    }

    //SEARCH

    public SearchCollection getSearch(String keywords, Operator operator, Filter filter) throws CytomineException {
        return getSearch(keywords, operator, filter, null);
    }

    public SearchCollection getSearch(String keywords, Operator operator, Filter filter, List<Long> idProjects) throws CytomineException {
        SearchCollection search = new SearchCollection(offset, max);
        if (operator == null) {
            operator = Operator.OR;
        }
        if (filter == null) {
            filter = Filter.ALL;
        }
        search.addFilter("keywords", buildEncode(keywords));
        search.addFilter("operator", buildEncode(operator + ""));
        search.addFilter("filter", buildEncode(filter + ""));
        if (idProjects != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < idProjects.size(); i++) {
                list.add(idProjects.get(i) + "");
            }
            search.addFilter("projects", Cytomine.join(list, ", "));
        }

        return (SearchCollection) search.fetch();
    }


    //:to do move this method into Utils package
    public static String join(ArrayList s, String delimiter) {
        if (s.size() == 0) return "";
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = s.iterator();
        while (iterator.hasNext()) {
            buffer.append(iterator.next());
            if (iterator.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    public String resetPassword(Long idUser, String newPassword) throws CytomineException {
		return getDefaultCytomineConnection().doPut("/api/user/" + idUser + "/password.json", "{password: "+newPassword+"}").toString();
    }

    public Description getDescription(Long domainIdent, String domainClassName) throws CytomineException {
        Description description = new Description();
        return description.fetch(domainClassName, domainIdent.toString());
    }


    public Description addDescription(Long domainIdent, String domainClassName, String text) throws CytomineException {
        Description description = new Description();
        description.set("domainIdent", domainIdent);
        description.set("domainClassName", domainClassName);
        description.set("data", text);
        return description.save();
    }

    public Description editDescription(Long domainIdent, String domainClassName, String text) throws CytomineException {
        Description description = getDescription(domainIdent, domainClassName);
        description.set("domainIdent", domainIdent);
        description.set("domainClassName", domainClassName);
        description.set("data", text);
        return description.update();
    }

    public void deleteDescription(Long domainIdent, String domainClassName) throws CytomineException {
        Description description = getDescription(domainIdent, domainClassName);
        description.set("domainIdent", domainIdent);
        description.set("domainClassName", domainClassName);
        description.delete();
    }

    public Map<String, Long> getRoleMap() throws CytomineException {
        Map<String, Long> map = new TreeMap<String, Long>();
        Collection<Role> roles = Collection.fetch(Role.class);
        for (int i = 0; i < roles.size(); i++) {
            map.put(roles.get(i).getStr("authority"), roles.get(i).getLong("id"));
        }
        return map;
    }

    public Role addRole(Long idUser, Long idRole) throws CytomineException {
        Role role = new Role();
        role.set("user", idUser + "");
        role.set("role", idRole + "");
        role.addFilter("user", idUser + "");
        return role.save();
    }


    public void deleteRole(Long idUser, Long idRole) throws CytomineException {
        Role role = new Role();
        role.set("user", idUser + "");
        role.set("role", idRole + "");
        role.addFilter("user", idUser + "");
        role.addFilter("role", idRole + "");
        role.delete();
    }

    public AbstractImage addNewImage(Long idUploadedFile, String path, String filename, String mimeType) throws CytomineException {
        return addNewImage(idUploadedFile, path, filename, null, mimeType);
    }

    public AbstractImage addNewImage(Long idUploadedFile, String path, String filename, String originalFilename, String mimeType) throws CytomineException {
        AbstractImage image = new AbstractImage();
        image.set("path",path);
        image.set("filename",filename);
        if(originalFilename != null) image.set("originalFilename",originalFilename);
        image.set("mimeType",mimeType);
        image.addFilter("uploadedFile", idUploadedFile + "");
        return image.save();
    }

    public UploadedFileCollection getUploadedFiles(boolean deleted) throws CytomineException {
        UploadedFileCollection files = new UploadedFileCollection(offset, max);
        files.addParams("deleted", "true");
        return (UploadedFileCollection)files.fetch();
    }


    public String getSimplified(String wkt, Long min, Long max) throws CytomineException {
        Annotation annotation = new Annotation();
        annotation.set("wkt", wkt);
        return getDefaultCytomineConnection().doPost("/api/simplify.json?minPoint=" + min + "&maxPoint=" + max, annotation.toJSON()).toString();
    }


    public void indexToRetrieval(Long id, Long container, String url) throws CytomineException {
        String data = "{id : " + id + ", container : " + container + ", url : '" + url + "'}";
        defaultCytomineConnection.doPost("/retrieval-web/api/resource.json", data);
    }
























    // Still here for backward compatibility

    @Deprecated
    public Project editProject(Long idProject, String name, Long idOntology) throws CytomineException {
        Project project = new Project().fetch(idProject);
        project.set("name", name);
        project.set("ontology", idOntology);
        return project.update();
    }

    @Deprecated
    public Ontology editOntology(Long idOntology, String name) throws CytomineException {
        Ontology ontology = new Ontology().fetch(idOntology);
        ontology.set("name", name);
        return ontology.update();
    }

    @Deprecated
    public Annotation editAnnotation(Long idAnnotation, String locationWKT) throws CytomineException {
        Annotation annotation = new Annotation().fetch(idAnnotation);
        annotation.set("location", locationWKT);
        return annotation.update();
    }

    @Deprecated
    public Term editTerm(Long idTerm, String name, String color, Long idOntology) throws CytomineException {
        Term term = new Term().fetch(idTerm);
        term.set("name", name);
        term.set("color", color);
        term.set("ontology", idOntology);
        return term.update();
    }

    @Deprecated
    public JobData editJobData(Long idJobData, String key, Long idJob, String filename) throws CytomineException {
        JobData jobData = new JobData().fetch(idJobData);
        jobData.set("key", key);
        jobData.set("job", idJob);
        jobData.set("filename", filename);
        return jobData.update();
    }

    @Deprecated
    public UploadedFile editUploadedFile(Long id, int status, boolean converted, Long idParent) throws CytomineException {
        UploadedFile uploadedFile = new UploadedFile().fetch(id);
        uploadedFile.set("status", status);
        uploadedFile.set("converted", converted);
        uploadedFile.set("parent", idParent);
        return uploadedFile.update();
    }

    @Deprecated
    public UploadedFile editUploadedFile(Long id, int status, boolean converted) throws CytomineException {
        UploadedFile uploadedFile = new UploadedFile().fetch(id);
        uploadedFile.set("status", status);
        uploadedFile.set("converted", converted);
        return uploadedFile.update();
    }

    @Deprecated
    public UploadedFile editUploadedFile(Long id, int status) throws CytomineException {
        UploadedFile uploadedFile = new UploadedFile().fetch(id);
        uploadedFile.set("status", status);
        return uploadedFile.update();
    }

    // Still here for backward compatibility
    // one line functions

    @Deprecated
    public Project getProject(Long id) throws CytomineException {
        return new Project().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(Project.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ProjectCollection getProjects() throws CytomineException {
        ProjectCollection projects = new ProjectCollection(offset, max);
        return (ProjectCollection) projects.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Project.class,Ontology.class,idOntology,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ProjectCollection getProjectsByOntology(Long idOntology) throws CytomineException {
        ProjectCollection projects = new ProjectCollection(offset, max);
        projects.addFilter("ontology", idOntology + "");
        return (ProjectCollection)projects.fetch();
    }
    /**
     * Deprecated. Use Collection.fetchWithFilter(Project.class,User.class,idUser,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ProjectCollection getProjectsByUser(Long idUser) throws CytomineException {
        ProjectCollection projects = new ProjectCollection(offset, max);
        projects.addFilter("user", idUser + "");
        return (ProjectCollection)projects.fetch();
    }

    @Deprecated
    public Project addProject(String name, Long idOntology) throws CytomineException {
        return new Project(name,idOntology).save();
    }

    @Deprecated
    public void deleteProject(Long idProject) throws CytomineException {
        new Project().delete(idProject);
    }

    @Deprecated
    public Ontology getOntology(Long id) throws CytomineException {
        return new Ontology().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(Ontology.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public OntologyCollection getOntologies() throws CytomineException {
        OntologyCollection ontologys = new OntologyCollection(offset, max);
        return (OntologyCollection)ontologys.fetch();
    }

    @Deprecated
    public Ontology addOntology(String name) throws CytomineException {
        return new Ontology(name).save();
    }

    @Deprecated
    public void deleteOntology(Long idOntology) throws CytomineException {
        new Ontology().delete(idOntology);
    }

    @Deprecated
    public AbstractImage addAbstractImage(String filename, String mime) throws CytomineException {
        return new AbstractImage(filename,mime).save();
    }

    @Deprecated
    public AbstractImage getAbstractImage(Long id) throws CytomineException {
        return new AbstractImage().fetch(id);
    }

    @Deprecated
    public ImageInstance getImageInstance(Long id) throws CytomineException {
        return new ImageInstance().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(ImageInstance.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ImageInstanceCollection getImageInstances(Long idProject) throws CytomineException {
        ImageInstanceCollection image = new ImageInstanceCollection(offset, max);
        image.addFilter("project", idProject + "");
        return (ImageInstanceCollection) image.fetch();
    }
    /**
     * Deprecated. Use Collection.fetchWithFilter(ImageInstance.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ImageInstanceCollection getImageInstancesByOffsetWithMax(Long idProject, int offset, int max) throws CytomineException {
        ImageInstanceCollection image = new ImageInstanceCollection(offset, max);
        image.addFilter("project", idProject + "");
        image.addFilter("offset", offset + "");
        image.addFilter("max", max + "");
        return (ImageInstanceCollection) image.fetch();
    }

    @Deprecated
    public ImageInstance addImageInstance(Long idAbstractImage, Long idProject) throws CytomineException {
        return new ImageInstance(idAbstractImage, idProject).save();
    }

    @Deprecated
    public void deleteImageInstance(Long idImageInstance) throws CytomineException {
        new ImageInstance().delete(idImageInstance);
    }

    @Deprecated
    public Annotation getAnnotation(Long id) throws CytomineException {
        return new Annotation().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(Annotation.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotations() throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        return (AnnotationCollection) annotations.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Annotation.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotationsByProject(Long idProject) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("project", idProject + "");
        return (AnnotationCollection) annotations.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Annotation.class,Term.class,idTerm,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotationsByTerm(Long idTerm) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("term", idTerm + "");
        return (AnnotationCollection) annotations.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Annotation.class,User.class,idUser,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotationsByUser(Long idUser) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("user", idUser + "");
        return (AnnotationCollection) annotations.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Annotation.class,Ontology.class,idOntology,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotationsByOntology(Long idOntology) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("ontology", idOntology + "");
        return (AnnotationCollection) annotations.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Annotation.class,ImageInstance.class,idImage,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AnnotationCollection getAnnotationsByImage(Long idImage) throws CytomineException {
        AnnotationCollection annotations = new AnnotationCollection(offset, max);
        annotations.addFilter("image", idImage + "");
        return (AnnotationCollection) annotations.fetch();
    }

    @Deprecated
    public Annotation addAnnotation(String locationWKT, Long image) throws CytomineException {
        return new Annotation(locationWKT,image).save();
    }

    @Deprecated
    public Annotation addAnnotationWithTerms(String locationWKT, Long image, List<Long> terms) throws CytomineException {
        return new Annotation(locationWKT,image,terms).save();
    }

    @Deprecated
    public Annotation addAnnotation(String locationWKT, Long image, Long project) throws CytomineException {
        return new Annotation(locationWKT,image,project).save();
    }

    @Deprecated
    public void deleteAnnotation(Long idAnnotation) throws CytomineException {
        new Annotation().delete(idAnnotation);
    }

    @Deprecated
    public Term getTerm(Long id) throws CytomineException {
        return new Term().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(Term.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public TermCollection getTerms() throws CytomineException {
        TermCollection terms = new TermCollection(offset, max);
        return (TermCollection) terms.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Term.class,Ontology.class,idOntology,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public TermCollection getTermsByOntology(Long idOntology) throws CytomineException {
        TermCollection terms = new TermCollection(offset, max);
        terms.addFilter("ontology", idOntology + "");
        return (TermCollection) terms.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Term.class,Annotation.class,idAnnotation,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public TermCollection getTermsByAnnotation(Long idAnnotation) throws CytomineException {
        TermCollection terms = new TermCollection(offset, max);
        terms.addFilter("annotation", idAnnotation + "");
        return (TermCollection) terms.fetch();
    }

    @Deprecated
    public Term addTerm(String name, String color, Long idOntology) throws CytomineException {
        return new Term(name,color,idOntology).save();
    }

    @Deprecated
    public void deleteTerm(Long idTerm) throws CytomineException {
        new Term().delete(idTerm);
    }

    @Deprecated
    public AnnotationTerm addAnnotationTerm(Long idAnnotation, Long idTerm) throws CytomineException {
        return new AnnotationTerm(idAnnotation,idTerm).save();
    }

    @Deprecated
    public AnnotationTerm addAnnotationTerm(Long idAnnotation, Long idTerm, Long idExpectedTerm, Long idUser, double rate) throws CytomineException {
        return new AnnotationTerm(idAnnotation,idTerm,idExpectedTerm,idUser,rate).save();
    }

    @Deprecated
    public User addUser(String username, String firstname, String lastname, String email, String password) throws CytomineException {
        return new User(username,firstname,lastname,email,password).save();
    }

    @Deprecated
    public User getUser(Long id) throws CytomineException {
        return new User().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(User.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public UserCollection getUsers() throws CytomineException {
        UserCollection users = new UserCollection(offset, max);
        return (UserCollection) users.fetch();
    }

    @Deprecated
    public UserJob getUserJob(Long id) throws CytomineException {
        return new UserJob().fetch(id);
    }

    @Deprecated
    public Job getJob(Long id) throws CytomineException {
        return new Job().fetch(id);
    }

    @Deprecated
    public void deleteSoftware(Long idSoftware) throws CytomineException {
        new Software().delete(idSoftware);
    }

    @Deprecated
    public SoftwareProject addSoftwareProject(Long idSoftware, Long idProject) throws CytomineException {
        return new SoftwareProject(idSoftware,idProject).save();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Software.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public SoftwareCollection getSoftwaresByProject(Long idProject) throws CytomineException {
        SoftwareCollection softwares = new SoftwareCollection(offset, max);
        softwares.addFilter("project", idProject + "");
        return (SoftwareCollection) softwares.fetch();
    }

    /**
     * Deprecated. Use Collection.fetch(Software.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public SoftwareCollection getSoftwares() throws CytomineException {
        SoftwareCollection softwares = new SoftwareCollection(offset, max);
        return (SoftwareCollection) softwares.fetch();
    }

    @Deprecated
    public ImageFilter addImageFilter(String name, String baseUrl, String processingServer) throws CytomineException {
        return new ImageFilter(name,baseUrl,processingServer).save();
    }

    public JobData getJobData(Long id) throws CytomineException {
        return new JobData().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(JobData.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public JobDataCollection getJobDatas() throws CytomineException {
        JobDataCollection jobDatas = new JobDataCollection(offset, max);
        return (JobDataCollection) jobDatas.fetch();
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(Software.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public JobDataCollection getJobDataByJob(Long idJob) throws CytomineException {
        JobDataCollection jobDatas = new JobDataCollection(offset, max);
        jobDatas.addFilter("job", idJob + "");
        return (JobDataCollection) jobDatas.fetch();
    }

    @Deprecated
    public JobData addJobData(String key, Long idJob, String filename) throws CytomineException {
        return new JobData(idJob,key,filename).save();
    }

    public void deleteJobData(Long idJobData) throws CytomineException {
        new JobData().delete(idJobData);
    }

    /**
     * Deprecated. Use Collection.fetchWithFilter(ReviewedAnnotation.class,Project.class,idProject,offset,max) instead
     * @throws CytomineException
     */
    @Deprecated
    public ReviewedAnnotationCollection getReviewedAnnotationsByProject(Long idProject) throws CytomineException {
        ReviewedAnnotationCollection reviewed = new ReviewedAnnotationCollection(offset, max);
        reviewed.addFilter("project", idProject + "");
        return (ReviewedAnnotationCollection) reviewed.fetch();
    }

    @Deprecated
    public Storage getStorage(Long id) throws CytomineException {
        return new Storage().fetch(id);
    }

    /**
     * Deprecated. Use Collection.fetch(Storage.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public StorageCollection getStorages() throws CytomineException {
        StorageCollection storages = new StorageCollection(offset, max);
        return (StorageCollection) storages.fetch();
    }

    @Deprecated
    public StorageAbstractImage addStorageAbstractImage(Long idStorage, Long idAbstractImage) throws CytomineException {
        return new StorageAbstractImage(idStorage,idAbstractImage);
    }

    @Deprecated
    public AbstractImage editAbstractImage(Long idAbstractImage, String originalFilename) throws CytomineException {
        AbstractImage image = new AbstractImage().fetch(idAbstractImage);
        image.set("originalFilename", originalFilename);
        return image.update();
    }

    @Deprecated
    public ImageGroup addImageGroup(Long idProject) throws CytomineException {
        return new ImageGroup(idProject).save();
    }

    @Deprecated
    public ImageSequence addImageSequence(Long idImageGroup, Long idImage, Integer zStack, Integer slice, Integer time, Integer channel) throws CytomineException {
        return new ImageSequence(idImageGroup,idImage,zStack,slice,time,channel).save();
    }

    /**
     * Deprecated. Use Collection.fetch(Role.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public RoleCollection getRoles() throws CytomineException {
        RoleCollection roles = new RoleCollection(offset, max);
        return (RoleCollection) roles.fetch();
    }

    @Deprecated
    public JobTemplate addJobTemplate(String name, Long iProject, Long idSoftware) throws CytomineException {
        return new JobTemplate(name,iProject,idSoftware).save();
    }

    @Deprecated
    public JobParameter addJobParameter(Long job, Long softwareParameter, String value) throws CytomineException {
        return new JobParameter(job,softwareParameter,value).save();
    }

    @Deprecated
    public UploadedFile addUploadedFile(String originalFilename, String realFilename, String path, Long size, String ext, String contentType, List idProjects, List idStorages, Long idUser, Long idParent) throws CytomineException {
        return addUploadedFile(originalFilename, realFilename, path, size, ext, contentType, idProjects, idStorages, idUser, -1l, idParent);
    }

    @Deprecated
    public UploadedFile addUploadedFile(String originalFilename, String realFilename, String path, Long size, String ext, String contentType, List idProjects, List idStorages, Long idUser, Long status, Long idParent) throws CytomineException {
        UploadedFile.Status state = Arrays.stream(UploadedFile.Status.values())
                .filter(c -> c.getCode() == status)
                .findFirst().get();
        return new UploadedFile(originalFilename,realFilename,path,size,ext,contentType,idProjects,idStorages,idUser,state,idParent).save();
    }

    @Deprecated
    public UploadedFile getUploadedFile(Long id) throws CytomineException {
        return new UploadedFile().fetch(id);
    }

    @Deprecated
    public void deleteUploadedFile(Long idUploadedFile) throws CytomineException {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.set("id", idUploadedFile);
        uploadedFile.delete();
    }

    /**
     * Deprecated. Use Collection.fetch(AmqpQueue.class,offset, max) instead
     * @throws CytomineException
     */
    @Deprecated
    public AmqpQueueCollection getAmqpQueue() throws CytomineException {
        AmqpQueueCollection queues = new AmqpQueueCollection(offset, max);
        return (AmqpQueueCollection) queues.fetch();
    }

    // TODO : remove this line when rest url of core are normalized
    public static String convertDomainName(String input){
        switch (input.toLowerCase()) {
            case "project" :
                return "be.cytomine.project.Project";
            case "imageinstance" :
                return "be.cytomine.image.ImageInstance";
            case "annotation" :
                return "be.cytomine.AnnotationDomain";
            default:
                try {
                    throw new CytomineException(400,"Client doesn't support other domain for now. Domain was "+input);
                } catch (CytomineException e) {
                    e.printStackTrace();
                    return "";
                }
        }
    }

}
