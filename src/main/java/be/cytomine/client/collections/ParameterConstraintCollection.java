package be.cytomine.client.collections;

import be.cytomine.client.models.ParameterConstraint;

public class ParameterConstraintCollection extends Collection<ParameterConstraint> {

    public ParameterConstraintCollection() {
        super(ParameterConstraint.class, 0, 0);
    }
    public ParameterConstraintCollection(int offset, int max) {
        super(ParameterConstraint.class, max, offset);
    }
}
