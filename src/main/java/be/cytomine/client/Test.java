package be.cytomine.client;

import be.cytomine.client.collections.Collection;
import be.cytomine.client.models.Model;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                if (code == 400 || code == 404) {
                    //err.append("Warning "+code+" : Collection<"+c.getSimpleName()+"> : fetch\n");
                } else {
                    err.append("Error 400 : Collection<"+c.getSimpleName()+"> : fetch\n");
                }
            }

            //call update
            runMethod(update,c,err);
            //call delete
            runMethod(delete,c,err);
        }
        System.out.println(err.toString());
    }

    private static void runMethod(Method m, Class c, StringBuilder err){
        try {
            if(m.getParameterCount() == 0) m.invoke(c.newInstance());
            if(m.getParameterCount() == 1) m.invoke(c.newInstance(),0L);
        } catch (InvocationTargetException e) {
            int code = ((CytomineException) e.getCause()).getHttpCode();
            if (code == 400 || code == 404) {
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
