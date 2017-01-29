# CloudTrail Viewer
CloudTrail Viewer is a Java Desktop Application for reading and analysing AWS CloudTrail Event logs.

# Update


**NOTE : 
The  code now in this repository is for Version 3 of the application.
While the application is still under development, the code should run and be usable within some limits.
There are many updates and tweaks that need to done before I will create a release for it.**

## Version 2

If you have come here looking for a working CloudTrail Viewer application you can download this from the
Releases tab -> [Releases](https://github.com/githublemming/CloudTrailViewer/releases)


## Version 3

### Getting Started
Version 3 has been completely rewritten from the ground up using the latest technologies and libraries. You should ensure
that you have the latest version of Java installed before attempting to compile and run the application.


Version 3 provides the capability to read files from the local files system as well as from an S3 bucket. Currently to 
access S3 with this version you will need to do two things.

1. Make sure that you have used the previous release of the application to create an AWS account via the preferences 
and entered the name of your CloudTrail S3 bucket

V3 currently does not use the API key and Secret that you configured in the above step instead if uses AWS SDK profiles. 
For more information about setting your computer up to use these read this 
[page](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html)

2. Create an entry on the credentials file like this (less the bullet points).

- [CloudTrailViewer]
- aws_access_key_id = YOUR_KEY
- aws_secret_access_key = YOUR_SECRET

### Using
Version 3 using a customisable Dashboard. 

![CloudTrail Viewer v3](/docs/ScreenShot.png)

When you first run the application a default dashboard will be created for you. Using the Dashboard menu you can add
additional 'widgets' to this, create a new dashboard, and save your changes. To remove a widget click the trash can icon
at the top right of the widget.