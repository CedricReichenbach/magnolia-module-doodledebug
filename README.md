# magnolia-module-doodledebug
A module for Magnolia CMS, integrating DoodleDebug

## Quick Start

1. Set up Magnolia ([official documentation](documentation.magnolia-cms.com/))
2. Build this module using [Maven](http://maven.apache.org/):
  On a command line, navigate to its directory and do `mvn clean package`.
3. Shut down Magnolia.
4. The built module jar is located in a sub-directory `target` and named `magnolia-module-doodledebug-x.x-SNAPSHOT.jar`.
  Copy it into each webapp's `WEB-INF/lib` directory of your Magnolia. For a default installation, those are `magnoliaAuthor` and `magnoliaPublic`, i.e. you need to put the module jar into `magnolia-x.x.x/apache-tomcat-x.x.x/magnoliaAuthor/WEB-INF/lib` and respectively the same for `magnoliaPublic`.
5. Into the same `WEB-INF/lib` directories, put the DoodleDebug jar and all its dependencies.
TODO: List and link everything
6. In order to attach DoodleDebug to log4j, adjust `WEB-INF/config/default/log4j.xml` for each webapp.
TODO: Put config snippet here
7. Start up Magnolia, navigate to each instance's web page and confirm the installation.
