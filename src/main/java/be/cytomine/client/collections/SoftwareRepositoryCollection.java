package be.cytomine.client.collections;

import be.cytomine.client.models.SoftwareRepository;
import org.json.simple.JSONObject;

public class SoftwareRepositoryCollection extends Collection {

    public SoftwareRepositoryCollection(int offset, int max) {
        super(offset, max);
    }

    @Override
    public String toURL() {
        return getJSONResourceURL();
    }

    @Override
    public String getDomainName() {
        return "software_repository";
    }

    public SoftwareRepository get(int i) {
        SoftwareRepository softwareRepository = new SoftwareRepository();
        Object item = list.get(i);
        softwareRepository.setAttr((JSONObject) item);
        return softwareRepository;
    }

}
