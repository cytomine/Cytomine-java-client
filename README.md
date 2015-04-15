# JavaClient
Cytomine Java client

# Presentation
Cytomine-java-client is an opensource Cytomine client. 
It is a JAR file that you can import in your JVM application (Java, Groovy, …).
You will be able to manage data in a Cytomine instance according to your credentials: manage users, add annotations, upload images,...
For more information about Cytomine, go to www.cytomine.be

Requirements:
* Java 1.7+
* Maven 2+ (only if you need to build a new Jar)

Binaries: https://github.com/cytomine/cytomine-java-client/releases

Source code: https://github.com/cytomine/cytomine-java-client

Full documentation: http://confluence.cytomine.be/display/DEVDOC/Part+5%3A+Cytomine+Java+Client

# How to use the Java-client
The java client is available with 2 jars:
* cytomine-java-client-*-jar-with-dependencies: A jar with all classes (client and dependencies).
* cytomine-java-client-*.jar: The jar only contains client classes without dependencies classes. A maven pom.xml is provided.

You need 3 paramters:
* CYTOMINE_URL: The full URL of the Cytomine core (“http://...cytomine.be”).
* PUBLIC_KEY: Your cytomine public key.
* PRIVATE_KEY: Your cytomine private key. 

You first need to init the connection object:    
    
    Cytomine cytomine = new Cytomine(CYTOMINE_URL, PUBLIC_KEY, PRIVATE_KEY);

Here is a sample code that should print “Hello ” with your Cytomine username and print the list of projects available.

    import be.cytomine.client.*
    import be.cytomine.client.models.*
    import be.cytomine.client.collections.*
  
    Cytomine cytomine = new Cytomine(CYTOMINE_URL, PUBLIC_KEY, PRIVATE_KEY);
    System.out.println("Hello " + cytomine.getCurrentUser().get("username"));
    System.out.println("******* You have access to these projects: *******");
    ProjectCollection projects = cytomine.getProjects();
    for(int i=0;i<projects.size();i++) {
        Project project = projects.get(i);
        System.out.println(project.get("name"));
    }
