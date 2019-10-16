package be.cytomine.client.collections;

import be.cytomine.client.models.DeleteCommand;

public class DeleteCommandCollection extends Collection<DeleteCommand>  {

    public DeleteCommandCollection(int max, int offset) {
        super(DeleteCommand.class, max, offset);
    }

    @Override
    public String getDomainName() {
        return "deletecommand";
    }
}
