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

import be.cytomine.client.models.User;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class CytomineConnection {

    private static final Logger log = LogManager.getLogger(Cytomine.class);

    private String host;
    private String login;
    private String pass;
    private String publicKey;
    private String privateKey;

    private User currentUser;
    //private String charEncoding = "UTF-8";

    /*
    private int max = 0;
    private int offset = 0;
*/

    private enum Method {GET, PUT, POST, DELETE}

    public CytomineConnection(String host, String publicKey, String privateKey) {
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

/*
    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
*/
    /**
     * Get the public key of this connection.
     * @author Philipp Kainz
     * @since
     * @return
     */
    public String getPublicKey() { return this.publicKey; }

    /**
     * Get the private key of this connection.
     * @author Philipp Kainz
     * @since
     * @return
     */
    public String getPrivateKey() { return this.privateKey; }


    public User getCurrentUser() throws CytomineException {
        return getCurrentUser(false);
    }

    public User getCurrentUser(boolean forceRefresh) throws CytomineException {
        if (forceRefresh || this.currentUser == null) {
            User user = new User();
            user.set("current", "current");
            currentUser = user.fetch(this, null);
        }

        return currentUser;
    }

    private void analyzeCode(int code, JSONObject json) throws CytomineException {

        //if 200,201,...no exception
        if (code >= 400 && code < 600) {
            throw new CytomineException(code, json);
        } else if (code == 301) {
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

    /*private JSONArray createJSONArrayResponse(int code, String response) throws CytomineException {
        Object obj = JSONValue.parse(response);
        return (JSONArray) obj;
    }*/

    public JSONObject doRequest(Method method, String URL, String data) throws CytomineException {
        HttpClient client = new HttpClient(publicKey, privateKey, getHost());
        try {
            client.authorize(method.toString(), URL, "", "application/json,*/*");
            client.connect(getHost() + URL);
            int code = 400;
            switch (method) {
                case GET:
                    code = client.get();
                    break;
                case POST:
                    code = client.post(data);
                    break;
                case DELETE:
                    code = client.delete();
                    break;
                case PUT:
                    code = client.put(data);
                    break;
            }
            String response = client.getResponseData();
            client.disconnect();
            JSONObject json = createJSONResponse(code, response);
            analyzeCode(code, json);
            return json;
        } catch(IOException e) {
            throw new CytomineException(e);
        }
    }

    public JSONObject doGet(String URL) throws CytomineException {
        return doRequest(Method.GET, URL, null);
    }
    public JSONObject doPost(String URL, String data) throws CytomineException {
        return doRequest(Method.POST, URL, data);
    }
    public JSONObject doDelete(String URL) throws CytomineException {
        return doRequest(Method.DELETE, URL, null);
    }
    public JSONObject doPut(String URL, String data) throws CytomineException {
        return doRequest(Method.PUT, URL, data);
    }

    //############### UPLOAD ###############
    public JSONObject uploadFile(String url, byte[] data, Map<String, String> entityParts) throws CytomineException {

        try {
            HttpClient client = null;
            MultipartEntity entity = new MultipartEntity();

            entity.addPart("files[]", new ByteArrayBody(data, new Date().getTime() + "file"));

            if(entityParts != null) {
                for (Map.Entry<String, String> entry : entityParts.entrySet()) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
                }
            }

            client = new HttpClient(publicKey, privateKey, getHost());
            client.authorize("POST", url, entity.getContentType().getValue(), "application/json,*/*");
            client.connect(getHost() + url);

            int code = client.post(entity);
            String response = client.getResponseData();
            log.debug("response=" + response);
            client.disconnect();
            JSONObject json = createJSONResponse(code, response);
            analyzeCode(code, json);
            return json;
        } catch (IOException e) {
            throw new CytomineException(e);
        }
    }
    public JSONObject uploadFile(String url, byte[] data) throws CytomineException {
        return this.uploadFile(url, data, null);
    }
    public JSONObject uploadFile(String url, File file, Map<String, String> entityParts) throws CytomineException {
        try {
            if(!entityParts.containsKey("filename")) entityParts.put("filename", file.getName());
            return uploadFile(url, Files.readAllBytes(file.toPath()), entityParts);
        } catch (IOException e) {
            throw new CytomineException(e);
        }
    }
    public JSONObject uploadFile(String url, File file) throws CytomineException {
        Map<String, String> entity = new HashMap<>();
        entity.put("filename", file.getName());
        return this.uploadFile(url, file, entity);
    }
    public JSONObject uploadFile(String url, String file) throws CytomineException {
        return this.uploadFile(url, new File(file));
    }

    public JSONArray uploadImage(String file, String url, Map<String, String> entityParts) throws CytomineException {
        try {
            HttpClient client = null;

            MultipartEntity entity = new MultipartEntity();

            for (Map.Entry<String, String> entry : entityParts.entrySet()) {
                entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
            }

            entity.addPart("files[]", new FileBody(new File(file)));

            client = new HttpClient(publicKey, privateKey, getHost());
            client.authorize("POST", url, entity.getContentType().getValue(), "application/json,*/*");
            client.connect(getHost() + url);
            int code = client.post(entity);
            String response = client.getResponseData();
            log.debug("response=" + response);
            client.disconnect();
            Object obj = JSONValue.parse(response);
            return (JSONArray) obj;
        } catch (IOException e) {
            throw new CytomineException(e);
        }
    }

    //############### DOWNLOAD FILES AND PICTURES ###############
    public void downloadPicture(String url, String dest) throws CytomineException {
        downloadPicture(url, dest, "jpg");
    }

    public void downloadPicture(String url, String dest, String format) throws CytomineException {
        HttpClient client = null;
        try {
            client = new HttpClient(publicKey, privateKey, getHost());
            BufferedImage img = client.readBufferedImageFromURL(url);
            ImageIO.write(img, format, new File(dest));

        } catch (Exception e) {
            throw new CytomineException(0, e.toString());
        }
    }

    public void downloadPictureWithRedirect(String url, String dest, String format) throws CytomineException {
        HttpClient client = null;
        try {
            client = new HttpClient(publicKey, privateKey, getHost());
            BufferedImage img = client.readBufferedImageFromRETRIEVAL(url, login, pass, getHost());
            ImageIO.write(img, format, new File(dest));
        } catch (Exception e) {
            throw new CytomineException(0, e.toString());
        }
    }

    public BufferedImage getPictureAsBufferedImage(String url, String format) throws CytomineException {
        HttpClient client = null;
        try {
            client = new HttpClient(publicKey, privateKey, getHost());
            return client.readBufferedImageFromURL(url);

        } catch (Exception e) {
            throw new CytomineException(0, e.toString());
        }
    }

    public void downloadFile(String url, String dest) throws CytomineException {

        try {
            HttpClient client = new HttpClient(publicKey, privateKey, getHost());
            int code = client.get(getHost() + url, dest);
            analyzeCode(code, (JSONObject) JSONValue.parse("{}"));
        } catch (Exception e) {
            throw new CytomineException(0, e.toString());
        }
    }


}
