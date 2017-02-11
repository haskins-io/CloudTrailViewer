# CloudTrail Viewer : Using the Source

To run the application from source you will need to either clone or download the code.

## Compiling the source using maven
CloudTrail viewer is a Maven application. There is a good introduction to maven on their website
https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html. If you are not familiar with Maven then I 
suggest you read their docs.

+ First you need to compile the code : mvn compile
+ Then you create a .jar file        : mvn package

Once you have created a .jar file then you should be able to double click on it to run the application. If that doesn't
work then try   `java -jar cloudtrailview.jar`

## Compiling using an IDE
If you have a Java IDE such as Eclipse, Netbeans or IntelliJ IDEA then you can create a new maven project and include
the downloaded source code.

Compile and run the code as you would any other Java project.