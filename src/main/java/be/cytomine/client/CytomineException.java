package be.cytomine.client;

/*
 * Copyright (c) 2009-2019. Authors: see NOTICE file.
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

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Exception triggered by Cytomine
 * This can be:
 * -forbiddenException (code = 403)
 * -objectNotFoundException (code = 404)
 * -...
 */
public class CytomineException extends Exception {

    private static final Logger log = Logger.getLogger(CytomineException.class);

    int httpCode;
    String message = "";

    public CytomineException(Exception e) {
        super(e);
        this.message = e.getMessage();
    }

    public CytomineException(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public CytomineException(int httpCode, JSONObject json) {
        this.httpCode = httpCode;
        getMessage(json);
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMsg() {
        return this.message;
    }

    private String getMessage(JSONObject json) {
        try {
            String msg = "";
            if (json != null) {
                Iterator iter = json.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    msg = msg + entry.getValue();
                }
            }
            message = msg;
        } catch (Exception e) {
            log.error(e);
        }
        return message;
    }

    public String toString() {
        return httpCode + " " + message;
    }
}

