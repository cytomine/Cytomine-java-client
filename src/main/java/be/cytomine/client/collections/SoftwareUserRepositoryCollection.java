package be.cytomine.client.collections;

import be.cytomine.client.models.SoftwareUserRepository;

public class SoftwareUserRepositoryCollection extends Collection<SoftwareUserRepository> {
    public SoftwareUserRepositoryCollection() {
        this(0,0);
    }
    public SoftwareUserRepositoryCollection(int offset, int max) {
        super(SoftwareUserRepository.class, max, offset);
    }
}
