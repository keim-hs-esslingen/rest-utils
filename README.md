# REST Utils

This project contains utility classes that are supposed to simplify dealing with REST-endpoints. It provides classes for calling REST-endpoints as well as for creating REST responses. Most classes provided in this package are built around the Spring REST framework and make use of its RestTemplate and ResponseEntity classes.

For this reason REST Utils depends on some of the classes from the Spring Boot framework. The version this project use internally in reflected in its own version number, of which the first three numbers are the same as the internally used Spring Boot version. Beginning with the fourth number, subversion are denoted, if e.g. updates are made to it's own code. This allows to easily select the appropriate REST Utils version for your Spring Boot project.