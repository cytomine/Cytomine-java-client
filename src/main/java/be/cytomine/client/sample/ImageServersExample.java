package be.cytomine.client.sample;

import org.apache.log4j.Logger;

import be.cytomine.client.Cytomine;
import be.cytomine.client.models.ImageInstance;

/**
 * This class tests the getImageServersForAbstractImage method in Cytomine class.
 * @author Daniel Felipe Gonzalez Obando
 */
public class ImageServersExample {
	private static final Logger log = Logger.getLogger(ImageServersExample.class);

	public static void testGetImageServers(Cytomine cytomine) throws Exception {

		ImageInstance image = cytomine.getImageInstance(20251732L);
		log.info("image instance = " + image.getId());
		log.info("abstract image = " + image.getLong("baseImage"));
		log.info(cytomine.getImageServersForAbstractImage(image.getLong("baseImage")));
	}
}
