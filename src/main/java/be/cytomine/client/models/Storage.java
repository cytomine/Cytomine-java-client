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


import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.StorageCollection;

public class Storage extends Model<Storage> {

    /**
     * Get the default storage of an user. If there is only one storage OR there is a storage with the username in the storage collection, we consider it as the default storage.
     *
     * @return a Storage
     * @throws Exception If no storage with the previously detailed conditions is found
     */
    public static Storage getCurrentUserStorage() throws CytomineException {
        StorageCollection sc = new StorageCollection();
        sc.fetch();
        if(sc.size() == 1) return sc.get(0);
        if(sc.size() > 1) {
            for(int i = 0; i < sc.size();i++){
                Storage s = sc.get(i);
                if(s.get("name").toString().equals(Cytomine.getInstance().getCurrentUser().get("username"))) return s;
            }
        }
        throw new CytomineException(404,"Cannot determine the storage of current user.");
    }
}
