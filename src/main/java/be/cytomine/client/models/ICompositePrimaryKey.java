package be.cytomine.client.models;

import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;

public interface ICompositePrimaryKey<T extends Model> {
    public T fetch(String key1, String key2) throws CytomineException;
    public T fetch(CytomineConnection connection, String key1, String key2) throws CytomineException;

}
