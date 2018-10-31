# Cytomine Java client

> Cytomine-java-client is an open-source Cytomine client written in Java. This client is a Java wrapper around Cytomine REST API gateway.

[![Build Status](https://travis-ci.com/Cytomine-ULiege/Cytomine-java-client.svg?branch=master)](https://travis-ci.com/Cytomine-ULiege/Cytomine-java-client)
[![GitHub release](https://img.shields.io/github/release/Cytomine-ULiege/Cytomine-java-client.svg)](https://github.com/Cytomine-ULiege/Cytomine-java-client/releases)
[![GitHub](https://img.shields.io/github/license/Cytomine-ULiege/Cytomine-java-client.svg)](https://github.com/Cytomine-ULiege/Cytomine-java-client/blob/master/LICENSE)

## Overview

The main access point to Cytomine data is its REST API. This client is a JAR file that can be imported in a JVM application (Java, Groovy, ...). It allows to import/export data from Cytomine-Core and Cytomine-IMS using RESTful web services e.g. to generate annotation (spatial) statistics, create regions of interest (e.g. tumor masks), add metadata to images/annotations, apply algorithms on image tiles, ...

See [documentation](http://doc.cytomine.be/display/ALGODOC/%5BDOC%5D+Data+access) for more details.

## Requirements
* Java 1.7+
* Maven 2+ (only if you need to build a new Jar)

## Install

**To install *official* release of Cytomine-java-client, see @cytomine. Follow this guide to install forked version by ULiege.** 

The java client is available with 2 jars:
* `cytomine-java-client-*-jar-with-dependencies.jar`: A jar with all classes (client and dependencies).
* `cytomine-java-client-*.jar`: The jar only contains client classes without dependencies classes. A maven pom.xml is provided.

### Manual installation
To download and install manually the package, see [manual installation procedure](http://doc.cytomine.be/display/ALGODOC/How+to+install+the+Cytomine+Java+Client).

### Automatic installation
To retrieve package using Maven or Gradle, see [package repository](https://packagecloud.io/cytomine-uliege/Cytomine-java-client).


## Usage

See [detailed usage documentation](http://doc.cytomine.be/display/DEVDOC/Part+5%3A+Cytomine+Java+Client).

### Basic example
Three parameters are required to connect:
* `CYTOMINE_URL`: The full URL of Cytomine core (e.g. “http://demo.cytomine.be”).
* `PUBLIC_KEY`: Your cytomine public key.
* `PRIVATE_KEY`: Your cytomine private key. 

First, the connection object has to be initialized.   
    
    Cytomine cytomine = new Cytomine(CYTOMINE_URL, PUBLIC_KEY, PRIVATE_KEY);

The next sample code should print “Hello {username}” where {username} is replaced by your Cytomine username and print the list of available projects.

    import be.cytomine.client.*
    import be.cytomine.client.models.*
    import be.cytomine.client.collections.*
  
    Cytomine cytomine = new Cytomine(CYTOMINE_URL, PUBLIC_KEY, PRIVATE_KEY);
    System.out.println("Hello " + cytomine.getCurrentUser().get("username"));
    System.out.println("******* You have access to these projects: *******");
    ProjectCollection projects = cytomine.getProjects();
    for(int i=0; i<projects.size(); i++) {
        Project project = projects.get(i);
        System.out.println(project.get("name"));
    }

## References
When using our software, we kindly ask you to cite our website url and related publications in all your work (publications, studies, oral presentations,...). In particular, we recommend to cite (Marée et al., Bioinformatics 2016) paper, and to use our logo when appropriate. See our license files for additional details.

- URL: http://www.cytomine.org/
- Logo: [Available here](https://cytomine.coop/sites/cytomine.coop/files/inline-images/logo-300-org.png)
- Scientific paper: Raphaël Marée, Loïc Rollus, Benjamin Stévens, Renaud Hoyoux, Gilles Louppe, Rémy Vandaele, Jean-Michel Begon, Philipp Kainz, Pierre Geurts and Louis Wehenkel. Collaborative analysis of multi-gigapixel imaging data using Cytomine, Bioinformatics, DOI: [10.1093/bioinformatics/btw013](http://dx.doi.org/10.1093/bioinformatics/btw013), 2016. 

## License

Apache 2.0