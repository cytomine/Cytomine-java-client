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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HTTP Client abstraction
 */
public class HttpClient {

    private static final Logger log = LogManager.getLogger(HttpClient.class);

    org.apache.http.client.HttpClient client;
    HttpHost targetHost;
    HttpClientContext localcontext;
    URL URL;
    HttpResponse response;
    Header[] headersArray;

    boolean isAuthByPrivateKey;

    String publicKey;
    String privateKey;
    String host;

    public HttpClient() {
    }


    public HttpClient(String publicKey, String privateKey, String host) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.host = host;
    }

    public void addHeader(String name, String value) {
        Header[] headers;
        if (headersArray != null) {
            headers = new Header[headersArray.length + 1];
        }
        else {
            headers = new Header[1];
        }

        for (int i = 0; i < headers.length - 1; i++) {
            headers[i] = headersArray[i];
        }
        headers[headers.length - 1] = new BasicHeader(name, value);
        headersArray = headers;
    }

    public void connect(String url, String username, String password) throws IOException {
        isAuthByPrivateKey = false;
        log.info("Connection to " + url + " with login=" + username + " and pass=" + password);
        URL = new URL(url);
        targetHost = new HttpHost(URL.getHost(), URL.getPort(), URL.getProtocol());
        client = HttpClientBuilder.create().build();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Set credentials
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(AuthScope.ANY, creds);

        // Add AuthCache to the execution context
        localcontext = HttpClientContext.create();
        localcontext.setCredentialsProvider(credsProvider);
        localcontext.setAuthCache(authCache);
    }

    public void connect(String url) throws IOException {
        log.info("Connection to " + url);
        isAuthByPrivateKey = true;
        URL = new URL(url);
        targetHost = null;
        if(url.substring(0,8).equals("https://")){
            targetHost = new HttpHost(URL.getHost(), 443, "https");
        } else {
            targetHost = new HttpHost(URL.getHost(), URL.getPort());
        }
        client = HttpClientBuilder.create().build();
        localcontext = HttpClientContext.create();

    }

    public int get() throws IOException {
        HttpGet httpGet = new HttpGet(URL.toString());
        if (isAuthByPrivateKey) httpGet.setHeaders(headersArray);
        response = client.execute(targetHost, httpGet, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int get(String url, String dest) throws IOException {
        return this.get(url, dest, true);
    }

    public int get(String url, String dest, boolean redirect) throws IOException {
        log.debug("get:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort(), URL.getProtocol());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();
        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);


        headersArray = null;
        authorize("GET", URL.toString(), "", "application/json,*/*");

        HttpGet httpGet = new HttpGet(URL.getPath());
        httpGet.setHeaders(headersArray);

        if(!redirect) {
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects",false);
            httpGet.setParams(params);
        }

        HttpResponse response = client.execute(targetHost, httpGet, localcontext);
        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) {
            throw new IOException(url + " cannot be read: " + code);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(dest);
            entity.writeTo(fos);
            fos.close();
        }

        if(isFound){
            Header locationHeader = response.getFirstHeader("location");
            if (locationHeader != null) {
                String redirectLocation = locationHeader.getValue();
                System.out.println("location: " + redirectLocation);
                this.get(redirectLocation,dest);
            } else code = HttpURLConnection.HTTP_NOT_FOUND;
        }
        return code;
    }

    public int delete() throws IOException {
        log.debug("Delete " + URL.getPath());
        HttpDelete httpDelete = new HttpDelete(URL.toString());
        if (isAuthByPrivateKey) {
            httpDelete.setHeaders(headersArray);
        }
        response = client.execute(targetHost, httpDelete, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int post(String data) throws IOException {
        log.debug("Post " + URL.getPath());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) {
            httpPost.setHeaders(headersArray);
        }
        log.debug("Post send :" + data.replace("\n", ""));
        //write data
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentLength((long) data.getBytes().length);
        httpPost.setEntity(entity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();
    }


    public int put(String data) throws IOException {
        log.debug("Put " + URL.getPath());
        HttpPut httpPut = new HttpPut(URL.toString());
        if (isAuthByPrivateKey) {
            httpPut.setHeaders(headersArray);
        }
        log.debug("Put send :" + data.replace("\n", ""));
        //write data
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentLength((long) data.getBytes().length);
        httpPut.setEntity(entity);
        response = client.execute(targetHost, httpPut, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int post(byte[] data) throws IOException {
        log.debug("POST " + URL.toString());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) {
            httpPost.setHeaders(headersArray);
        }
        log.debug("Post send :" + data.length);

        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), data.length);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);
        BufferedHttpEntity myEntity = null;
        try {
            myEntity = new BufferedHttpEntity(reqEntity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error(e);
        }

        httpPost.setEntity(myEntity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int post(MultipartEntity entity) throws IOException {
        log.debug("POST " + URL.toString());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) {
            httpPost.setHeaders(headersArray);
        }
        log.debug("Post send :" + entity);
        httpPost.setEntity(entity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();

    }


    public int put(byte[] data) throws IOException {
        log.debug("Put " + URL.getPath());
        HttpPut httpPut = new HttpPut(URL.getPath());
        if (isAuthByPrivateKey) {
            httpPut.setHeaders(headersArray);
        }
        log.debug("Put send :" + data.length);

        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), data.length);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);

        BufferedHttpEntity myEntity = null;
        try {
            myEntity = new BufferedHttpEntity(reqEntity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error(e);
        }

        httpPut.setEntity(myEntity);
        response = client.execute(targetHost, httpPut, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int download(String dest) throws IOException {
        log.debug("Download " + URL.getPath());
        HttpGet httpGet = new HttpGet(URL.getPath());
        if (isAuthByPrivateKey) {
            httpGet.setHeaders(headersArray);
        }
        response = client.execute(targetHost, httpGet, localcontext);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(dest);
            entity.writeTo(fos);
            fos.close();
        }
        return response.getStatusLine().getStatusCode();
    }

    public BufferedImage readBufferedImageFromURL(String url) throws IOException {
        log.debug("readBufferedImageFromURL:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort(), URL.getProtocol());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();

        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);

        headersArray = null;
        authorize("GET", URL.toString(), "", "application/json,*/*");

        BufferedImage img = null;
        HttpGet httpGet = new HttpGet(URL.toString());
        httpGet.setHeaders(headersArray);
        HttpResponse response = client.execute(targetHost, httpGet, localcontext);

        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(url + " cannot be read: " + code);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            img = ImageIO.read(entity.getContent());
        }
        return img;

    }

    public static BufferedImage readBufferedImageFromPOST(String url, String post) throws IOException{
        log.debug("readBufferedImageFromURL:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort(), URL.getProtocol());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();

        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);

        BufferedImage img = null;
        HttpPost httpPost = new HttpPost(URL.toString());
        httpPost.setEntity(new StringEntity(post, "UTF-8"));
        HttpResponse response = client.execute(targetHost, httpPost, localcontext);

        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(url + " cannot be read: " + code);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            img = ImageIO.read(entity.getContent());
        }
        return img;
    }


    public static BufferedImage readBufferedImageFromRETRIEVAL(String url, String publicKey, String privateKey, String host) throws IOException {
        log.debug("readBufferedImageFromURL:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort(), URL.getProtocol());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();
        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);
        Header[] headers = authorizeFromRETRIEVAL("GET", URL.toString(), "", "", publicKey, privateKey, host);
        log.debug("headers:" + headers.length);

        BufferedImage img = null;
        HttpGet httpGet = new HttpGet(URL.toString());
        httpGet.setHeaders(headers);
        HttpResponse response = client.execute(targetHost, httpGet, localcontext);
        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) {
            throw new IOException(url + " cannot be read: " + code);
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            log.debug("img=" + entity.getContent());
            img = ImageIO.read(entity.getContent());
        }
        return img;

    }

    public static Header[] authorizeFromRETRIEVAL(String action, String urlFullStr, String contentType, String accept, String publicKey, String privateKey, String host) throws IOException {
        log.debug("authorize: action=" + action + ", url=" + urlFullStr + ", contentType=" + contentType + ",accept=" + accept);
        String url = urlFullStr.replace(host, "");
        log.debug("authorize: url short=" + url);
        Header[] headers = new Header[3];
        headers[0] = new BasicHeader("accept", accept);
        headers[1] = new BasicHeader("date", getActualDateStr());

        String canonicalHeaders = action + "\n\n" + contentType + "\n" + headers[1].getValue() + "\n";
        String messageToSign = canonicalHeaders + url;
        SecretKeySpec privateKeySign = new SecretKeySpec(privateKey.getBytes(), "HmacSHA1");

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(privateKeySign);
            byte[] rawHmac = mac.doFinal(new String(messageToSign.getBytes(), "UTF-8").getBytes());
            //byte[] signatureBytes = Base64.encodeToByte(rawHmac,false);
            byte[] signatureBytes = org.apache.commons.codec.binary.Base64.encodeBase64(rawHmac, false);
            String authorization = "CYTOMINE " + publicKey + ":" + new String(signatureBytes);
            headers[2] = new BasicHeader("authorization", authorization);
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }

        return headers;
    }


    public void authorize(String action, String url, String contentType, String accept) throws IOException {
        url = url.replace(host, "");
        url = url.replace("http://" + host, "");
        url = url.replace("https://" + host, "");

        TreeMap<String, String> headers = new TreeMap<String, String>();
        headers.put("accept", accept);
        headers.put("date", getActualDateStr());

        log.debug("AUTHORIZE: " + action + "\\n\\n" + contentType + "\\n" + headers.get("date") + "\n");

        String canonicalHeaders = action + "\n\n" + contentType + "\n" + headers.get("date") + "\n";

        String messageToSign = canonicalHeaders + url;

        log.debug("publicKey=" + publicKey);
        log.debug("privateKey=" + privateKey);
        log.debug("messageToSign=" + messageToSign);


        SecretKeySpec privateKeySign = new SecretKeySpec(privateKey.getBytes(), "HmacSHA1");

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(privateKeySign);
            byte[] rawHmac = mac.doFinal(new String(messageToSign.getBytes(), "UTF-8").getBytes());

            byte[] signatureBytes = Base64.encodeBase64(rawHmac);

            String signature = new String(signatureBytes);

            String authorization = "CYTOMINE " + publicKey + ":" + signature;

            log.debug("signature=" + signature);
            log.debug("authorization=" + authorization);

            headers.put("authorization", authorization);

            for (String key : headers.keySet()) {
                addHeader(key, headers.get(key));
            }

        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }

    }

    public static String getActualDateStr()  {
        Date today = Calendar.getInstance().getTime();
        return new SimpleDateFormat("%E, %d %M %Y %H:%M:%S +0000").format(today);
    }


    public String getResponseData() throws IOException {
        HttpEntity entityResponse = response.getEntity();
        return IOUtils.toString(entityResponse.getContent());
    }

    public int getResponseCode()  {
        return response.getStatusLine().getStatusCode();
    }

    public void disconnect() {
        log.debug("Disconnect");
        try {
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
