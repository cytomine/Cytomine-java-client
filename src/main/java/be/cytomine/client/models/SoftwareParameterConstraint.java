package be.cytomine.client.models;

import be.cytomine.client.CytomineException;

public class SoftwareParameterConstraint extends Model<SoftwareParameterConstraint> {

    public SoftwareParameterConstraint(){}

    public SoftwareParameterConstraint(Long parameterConstraintId, Long softwareParameterId, String value) throws CytomineException {
		this.set("parameterConstraint", parameterConstraintId);
		this.set("softwareParameter", softwareParameterId);
		this.set("value", value);
	}
}
