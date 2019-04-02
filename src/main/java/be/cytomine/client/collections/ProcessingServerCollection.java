package be.cytomine.client.collections;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineConnection;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Model;
import be.cytomine.client.models.ProcessingServer;

public class ProcessingServerCollection extends Collection<ProcessingServer> {

    public ProcessingServerCollection(int offset, int max) {
        super(ProcessingServer.class, max, offset);
    }
}
