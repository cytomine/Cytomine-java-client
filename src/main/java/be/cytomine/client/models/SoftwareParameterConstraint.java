package be.cytomine.client.models;

import be.cytomine.client.CytomineException;

public class SoftwareParameterConstraint extends Model<SoftwareParameterConstraint> {

    public SoftwareParameterConstraint(){}
    public SoftwareParameterConstraint(String value){
        this.set("value", value);
    }

    public SoftwareParameterConstraint addSoftwareParameterConstraint(Long parameterConstraintId, Long softwareParameterId, String value) throws CytomineException {
		SoftwareParameterConstraint softwareParameterConstraint = new SoftwareParameterConstraint();
		softwareParameterConstraint.set("parameterConstraint", parameterConstraintId);
		softwareParameterConstraint.set("softwareParameter", softwareParameterId);
		softwareParameterConstraint.set("value", value);
		return softwareParameterConstraint.save();
	}
}
