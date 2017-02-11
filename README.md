# CloudTrail Viewer
CloudTrail Viewer is a Java Desktop Application for reading and analysing AWS CloudTrail Event logs.

## Version 2

If you have come here looking for a stable working CloudTrail Viewer application you can download this from the
Releases tab -> [Releases](https://github.com/githublemming/CloudTrailViewer/releases)


## Version 3

Version three is currently in Beta and is not yet finished or bug free. Having said this however it does work.

### Getting Started
Version 3 has been completely rewritten from the ground up using the latest technologies and libraries. You should ensure
that you have the latest version of Java installed before attempting to compile and run the application.


### Compiling from Source
There are instructions in /docs/RunningFromSource.md to get the application working form source.

Essentially CloudTrail Viewer is a Maven application, and if you are familiar with builindg and running applications
this way then you shouldn't have a problem.

### Using
Version 3 using a customisable Dashboard. 

![CloudTrail Viewer v3](/docs/ScreenShot.png)

When you first run the application a default dashboard will be created for you. Using the Dashboard menu you can add
additional 'widgets' to this, create a new dashboard, and save your changes. To remove a widget click the trash can icon
at the top right of the widget, you need to save the dashboard if you want to permanently remove it.

While the application is being finalised you can find more detailed information about the application in 
/docs/UserGuide.md. If you are downloading a Release then the files is available on the Help menu.
