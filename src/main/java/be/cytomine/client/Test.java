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

import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.Model;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Test {

    public static void launch() throws NoSuchMethodException {
        Reflections reflections = new Reflections("be.cytomine.client.models");
        Set<Class<? extends Model>> classes = reflections.getSubTypesOf(Model.class);

        StringBuilder err = new StringBuilder();
        Method save = Model.class.getDeclaredMethod("save");
        Method fetch = Model.class.getDeclaredMethod("fetch", Long.class);
        Method update = Model.class.getDeclaredMethod("update");
        Method delete = Model.class.getDeclaredMethod("delete");
        for (Class c : classes) {
            //call save, get the return
            runMethod(save,c,err);
            //call fetch
            runMethod(fetch,c,err);
            //call collection list
            try {
                Collection.fetch(c,0,0);
            } catch (CytomineException e) {
                int code = e.getHttpCode();
                // 403 : Forbidden ==> not enough right : ok
                if (code != 403) {
                    err.append("Error "+code+" : Collection<"+c.getSimpleName()+"> : fetch\n");
                }
            }

            //call update
            runMethod(update,c,err);
            //call delete
            runMethod(delete,c,err);
        }
        System.out.println("\n"+err.toString());
    }

    private static void runMethod(Method m, Class c, StringBuilder err){

        List<Integer> toleratedCodes = new ArrayList<>();
        // 400 : Bad Request ==> not enough information to create the object
        // 403 : Forbidden   ==> not enough right
        // 404 : Not found   ==> not enough information in the request
        // we will not catch the non existing urls :/
        toleratedCodes.add(403);
        toleratedCodes.add(404);
        if(m.getName().equals("save")){
            toleratedCodes.add(400);
        }

        try {
            if(m.getParameterCount() == 0) m.invoke(c.newInstance());
            if(m.getParameterCount() == 1) m.invoke(c.newInstance(),0L);
        } catch (InvocationTargetException e) {
            int code = ((CytomineException) e.getCause()).getHttpCode();
            if (toleratedCodes.contains(code)){
                //err.append("Warning "+code+" : "+c.getSimpleName()+" : "+m.getName()+"\n");
            } else {
                err.append("Error "+code+" : "+c.getSimpleName()+" : "+m.getName()+"\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
