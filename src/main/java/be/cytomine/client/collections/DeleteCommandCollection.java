package be.cytomine.client.collections;

public class DeleteCommandCollection extends Collection  {

    public DeleteCommandCollection(int max, int offset) {
        super(max, offset);
    }

    @Override
    public String toURL() {
        return getJSONResourceURL();
    }

    @Override
    public String getDomainName() {
        return "deletecommand";
    }
}
