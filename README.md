# CloudTrail Viewer
CloudTrail Viewer is a Java Desktop Application for reading and analysing AWS CloudTrail Event logs.

### Getting Started
Version 3 has been completely rewritten from the ground up using the latest technologies and libraries. 

You should ensure that you have the latest version of Java (minimum 8) installed before attempting to compile or run
the application. JavaFX is used to draw the interface and is bundled with Oracle Java 8 and higher. If
you use another version of Java, such as OpenJDK, then you will need to install the JavaFX libraries yourself.


## Running the application
You can run the application in one of two ways:

* you can either download(clone/fork) the source, compile it and run it
* you can download the prebuilt .jar from releases and run that. 


### Running from Code
The application is a Java 1.8 application that uses Maven as a project manager. If you are running the code from a Java 
IDE such as IntelliJ then you will be prompted to download the project dependencies if you don't already have them installed.


#### Running the .Jar file
The .jar created when building the application from source, (or downloaded from the release section on GitHub) is 
executable, and on some operating systems when double clicked will automatically run. If you have problems with this
then you can use the following command to run the application from a terminal.

``` java -jar cloudtrail-viewer.jar ```


### Application configuration database
The first time you run the application it will attempt to create a preferences database on your computer. This database
 will be placed into your OS defined UserHome folder for example on linux this would be:

* /home/joe.blogs/.cloudtrailviewer/prefs.db


### Compiling from Source
There are instructions in _/docs/RunningFromSource.md_ to get the application working form source.


### Using
Version 3 using a customisable Dashboard. 

![CloudTrail Viewer v3](/docs/ScreenShot.png)

When you first run the application a default dashboard will be created for you. You can add additional 'widgets' to this,
 create a new dashboard, and save your changes. To remove a widget click the trash can icon
at the top right of the widget, you need to save the dashboard if you want to permanently remove it.

While the application is being finalised you can find more detailed information about the application in 
_/docs/UserGuide.md_. If you are downloading a Release then this file is available on the Help menu.
