package be.cytomine.client.sample;

import org.apache.log4j.Logger;

import be.cytomine.client.Cytomine;
import be.cytomine.client.models.AbstractImage;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.ImageServers;

/**
 * This class tests the getImageServersForAbstractImage method in Cytomine
 * class.
 * 
 * @author Daniel Felipe Gonzalez Obando
 */
public class ImageServersExample {
	private static final Logger log = Logger.getLogger(ImageServersExample.class);

	public static void testGetImageServers(Cytomine cytomine) throws Exception {

		// Option one: from image instance
		ImageInstance image = cytomine.getImageInstance(20251732L);
		log.info("image instance = " + image.getId());
		ImageServers servers = cytomine.getImageInstanceServers(image);
		log.info(servers.getServerList());
		log.info(servers.getServerList().get(0));

		// Option two: from abstract image
		AbstractImage absImage = cytomine.getAbstractImage(image.getLong("baseImage"));
		log.info("abstract image = " + absImage.getId());
		servers = cytomine.getAbstractImageServers(absImage);
		log.info(servers.getServerList());
		log.info(servers.getServerList().get(0));
	}
}
