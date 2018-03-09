/*
 * Copyright (c) 2009-2016. Authors: see NOTICE file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.cytomine.client;

import be.cytomine.client.collections.SoftwareRepositoryCollection;
import be.cytomine.client.models.SoftwareRepository;
import be.cytomine.client.sample.SoftwareRepositoryExample;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Execute {

	private static final Logger log = Logger.getLogger(Execute.class);

	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		PropertyConfigurator.configure("log4j.properties");
		log.info("Connection to cytomine...");

		//Cytomine cytomine = new Cytomine(args[0], args[1], args[2]);

		Cytomine cytomine = new Cytomine("http://localhost:8080",
				"530b966f-d828-4983-bc8d-05b8a7ece9cf",
				"5156fa05-bdc9-402a-9a1f-4523066aa97b");
		ping(cytomine);
		// cytomine.addUserJob(104606215l,16l,57l,new Date(),104606261l);
		// cytomine.addUserJob(104606215l,16l,57l,new Date(),null);

		// SoftwareExample.testAddJobTemplate2(cytomine);
		// SoftwareExample.testAddJobTemplate3(cytomine);
		// SoftwareExample.testAddJobTemplate4(cytomine);
		//ImageServersExample.testGetImageServers(cytomine);

		//SoftwareRepositoryExample.addCytomineRepository(cytomine);
		//SoftwareRepositoryExample.addNeubiasRepository(cytomine);
		//SoftwareRepositoryExample.addSampleRepository(cytomine);

		SoftwareRepositoryCollection softwareRepositoryCollection = SoftwareRepositoryExample.getSoftwareRepositories(cytomine);
		for (int i = 0; i < softwareRepositoryCollection.size(); i++) {
			SoftwareRepository current = softwareRepositoryCollection.get(i);

			System.out.println("SOFTWARE REPOSITORY " + (i + 1));
			System.out.println("Id              : " + current.getStr("id"));
			System.out.println("Provider        : " + current.getStr("provider"));
			System.out.println("Repository User : " + current.getStr("repositoryUser"));
			System.out.println("" + current.getStr("id"));
		}

		cytomine.removeSoftwareRepository(2307);

	}

	public static void ping(Cytomine cytomine) throws CytomineException {
		log.info("Hello " + cytomine.getCurrentUser().get("username"));
	}
}
