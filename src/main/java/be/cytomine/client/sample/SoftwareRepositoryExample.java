package be.cytomine.client.sample;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.SoftwareRepositoryCollection;
import be.cytomine.client.models.SoftwareRepository;
import org.apache.log4j.Logger;

public class SoftwareRepositoryExample {

    private static final Logger log = Logger.getLogger(SoftwareExample.class);

    public static void addCytomineRepository(Cytomine cytomine) throws Exception {
        try {
            SoftwareRepository softwareRepository = cytomine
                    .addSoftwareRepository("GitHub","Cytomine", "w_", "add_cytomine_software.py");
        } catch (CytomineException ce) {
            log.error(ce);
        }
    }

    public static void addNeubiasRepository(Cytomine cytomine) throws Exception {
        try {
            SoftwareRepository softwareRepository = cytomine
                    .addSoftwareRepository("GitHub", "Neubias-WG5", "w_", "add_cytomine_software.py");
        } catch (CytomineException ce) {
            log.error(ce);
        }
    }

    public static void addSampleRepository(Cytomine cytomine) throws Exception {
        try {
            SoftwareRepository softwareRepository = cytomine
                    .addSoftwareRepository("Sample", "Sample", "s_", "sample.py");
        } catch (CytomineException ce) {
            log.error(ce);
        }
    }

    public static SoftwareRepositoryCollection getSoftwareRepositories(Cytomine cytomine) throws Exception {
        try {
            return cytomine.getSoftwareRepositories();
        } catch (CytomineException ce) {
            log.error(ce);
        }
        return null;
    }



}
