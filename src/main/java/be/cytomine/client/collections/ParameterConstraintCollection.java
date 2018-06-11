package be.cytomine.client.collections;

import be.cytomine.client.models.ParameterConstraint;
import be.cytomine.client.models.Software;
import org.json.simple.JSONObject;

public class ParameterConstraintCollection extends Collection {

    public ParameterConstraintCollection(int offset, int max) {
        super(offset, max);
    }

    @Override
    public String toURL() {
        return getJSONResourceURL();
    }

    @Override
    public String getDomainName() {
        return "parameter_constraint";
    }

    public ParameterConstraint get(int i) {
        ParameterConstraint parameterConstraint = new ParameterConstraint();
        Object item = list.get(i);
        parameterConstraint.setAttr((JSONObject) item);
        return parameterConstraint;
    }
}
