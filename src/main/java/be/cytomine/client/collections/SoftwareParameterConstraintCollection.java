package be.cytomine.client.collections;

import be.cytomine.client.models.SoftwareParameterConstraint;

public class SoftwareParameterConstraintCollection extends Collection<SoftwareParameterConstraint> {

    public SoftwareParameterConstraintCollection() {
        this(0,0);
    }
    public SoftwareParameterConstraintCollection(int offset, int max) {
        super(SoftwareParameterConstraint.class, max, offset);
    }
}
