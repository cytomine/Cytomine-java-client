package be.cytomine.client.models;

public class ParameterConstraint extends Model<ParameterConstraint>{

    public ParameterConstraint(){}
    public ParameterConstraint(String name,String expression, String dataType){
        this.set("name",name);
        this.set("expression",expression);
        this.set("dataType",dataType);
    }
}
