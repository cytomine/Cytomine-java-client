package be.cytomine.client.models;

import java.util.List;

public class SoftwareUserRepository extends Model<SoftwareUserRepository> {
    public SoftwareUserRepository(){}
    public SoftwareUserRepository(String provider, String username, String prefix){
        this.set("provider", provider);
        this.set("username", username);
        this.set("prefix", prefix);
    }

    @Override
    public String getDomainName() {
        return "software_user_repository";
    }
}
