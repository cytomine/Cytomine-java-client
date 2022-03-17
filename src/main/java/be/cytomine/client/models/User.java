package be.cytomine.client.models;

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

import be.cytomine.client.CytomineException;

public class User extends Model<User> {

    public User(){}
    public User(String username, String firstname, String lastname, String email, String password){
        this.set("username", username);
        this.set("firstname", firstname);
        this.set("lastname", lastname);
        this.set("email", email);
        this.set("password", password);
    }

    public static User getCurrent() throws CytomineException {
        User user = new User();
        user.set("current", "current");
        return user.fetch(null);
    }

    public boolean isHuman() throws CytomineException {
        return this.get("algo") == null;
    }

    @Override
    public String toURL() {

        if (getLong("id") != null) {
            return getJSONResourceURL();
        /*} else if (getStr("username get") != null) {
            return getJSONResourceURL(getStr("username get")); ???*/
        } else if (getStr("current") != null) {
            return "/api/user/current.json";
        } else if (isFilterBy("publicKeyFilter")) {
            return "/api/userkey/" + getFilter("publicKeyFilter") + "/keys.json";
        } else if (isFilterBy("id") && isFilterBy("keys")) {
            return "/api/user/" + getFilter("id") + "/keys.json";
        } else {
            return getJSONResourceURL();
        }
    }
}
