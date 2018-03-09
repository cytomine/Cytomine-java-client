package be.cytomine.client.collections;

import be.cytomine.client.models.ProcessingServer;
import org.json.simple.JSONObject;

public class ProcessingServerCollection extends Collection {

    public ProcessingServerCollection(int offset, int max) {
        super(max, offset);
    }

    @Override
    public String toURL() {
        return getJSONResourceURL();
    }

    @Override
    public String getDomainName() {
        return "processing_server";
    }

    public ProcessingServer get(int i) {
        ProcessingServer processingServer = new ProcessingServer();
        Object item = list.get(i);
        processingServer.setAttr((JSONObject) item);
        return processingServer;
    }

}
