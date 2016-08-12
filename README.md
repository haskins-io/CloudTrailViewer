# CloudTrail Viewer
CloudTrail Viewer is a Java Desktop Application for reading and analysing AWS CloudTrail Event logs.

## New Feature
Displaying of API Metrics.

You can see see (and drill down by time) the frequency of the API calls for the loaded event. You can either see all the events or filter by AWS service.


## Running the application
You can run the application in one of two ways:

* you can either download(clone/fork) the source, compile it and run it
* you can download the prebuilt .jar from releases and run that. Note you will need to have Java 7 (or higher) installed for the application to work.

### Running from Code
The application is a Java 1.7 application that uses Maven as a project manager. If you are running the code from a Java IDE such as Netbeans then you will be prompted to download the project dependencies if you don't already have them installed.

When performing a build Maven will create three binaries for the application:

* OS X - This has been tested using Java 1.7 and Java 1.8 but the JavaAppLauncher unix binary that is executed when you run the application has been known to crash.
* Windows : I don’t have a windows computer so this has not been tested fully but has worked on Windows 7 running in a VM.
* Jar : This is an executable jar.

#### Running the .Jar file
The .jar file is an executable and on some operating systems when double clicked will automatically run. If you have problems with this then you can using the following command to run the application from a terminal.

``` java -jar cloudtrail-viewer.jar ```

Note you will need to have Java 7 (or higher) installed for the application to work. 

### Application configuration database
The first time you run the application it will attempt to create a preferences database on your computer. This database will be placed into your OS defined UserHome folder for example on linux this would be:

* /home/joe.blogs/.cloudtrailviewer/prefs.db

## Loading Events
The first thing you will need to do once the application is running is to load some CloudTrial logs files, you can either load these from your local filesystem or from an S3 bucket.

### Local Files
To load files from the load filesystem click the left hand icon in the toolbar that looks like a folder. Browse to the required files, select the ones you need and click the load button. **Note the files must be the .gz format generated via CloudTrial**

You can select a folder and all files within that folder will be loaded.

When loading files all Events contained within the files will be loaded, depending on how active your AWS Account is you may find that loading more that 100 files can make the application unresponsive.

### S3 Files
To load files from an S3 bucket click on the icon that looks like a cloud beside the 'local files' icon. This presents a file chooser style dialog that allows you to navigate through an S3 bucket and load log files. The time it takes for the files to download and load will depend on your network connection and speed.

Like loading files from the local filesystem loading large amounts of files can cause problems.

#### Adding AWS API Credentials
To load files from a S3 bucket you need to provide a set of AWS API credentials that have the relevant permissions to read files from the bucket. You do this via the Preferences.

On the AWS tab of the Preferences Dialog click the + button in the top section (AWS Accounts) and enter your information. You will need to provide the following:

* a friendly name, this is not used it's for your reference
* the name of the bucket that contains the S3 files
* Your API Key
* Your API Secret

You can leave the Account field blank.


In order to be able to load the files from S3 you will need a IAM User that has the following permissions.
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:Get*",
        "s3:List*"
      ],
      "Resource": "arn:aws:s3:::cloudtrail-log*"
    }
  ]
}
```

### Scanning
As mentioned above loading a large amount of complete files can cause memory issues, an alternative method of loading files is by using the Scan buttons, these can be identified as these have a magnifying glass on the icon.

You use the panel of the left side of the S3 File Chooser dialog to define the filters to be applied when the Events are being loaded, only Events that match the filter will be loaded. This allows you to scan 100s of files quickly and in a more memory efficient manner.

## Working with loaded files
### Table View
The main component within the application is the Table View. You can select it from the toolbar that contains all the Events loaded into the applicaiton. Other features also utilise a Table View to show specifc subset of data.   

The Table View shows a subset of loaded Events and allows you to:

* Search the events in the table using the filter box above the table.
* View details about certain service resources such as ELB configurations and EC2 instances.
* Export the contents of the table as a CSV
* Change the visible table columns
* Selecting an event will open the side bar where you can:
    * View original Event JSON
    * View the Event tree representation
    * View visual representation of the loaded events

When looking at the Tree representation of the Event it is possible to double click on the resource that was actioned by the Event to get more information about it. When looking at a ELB CreateLoadBalancer Event under the Request Parameters node will be a node called 'Elastic LoadBalancer' which will contain the name of the ELB that was created. If you double click on the name of the ELB a dialog will appear showing information about that Load Balancer.

If you double click on a resource and no Account is associated with the AWS account number an error message will be displayed.

**If there is a problem retrieving the AWS resource information then an AWS error message will be displayed**

**Note**
In order for this feature to work you need to have created an AWS Account in the Preferences Dialog that contains the AWS Account Number, or added the account number to your account that has access to the S3 Bucket. 

This functionality also requires additional permissions in order to retrieve the information, this is in general List* and Describe* though to look at IAM objects you would need to also include Get*.

### Features
Features provide a means of breaking down events into specific groups to aid analysis and finding certain types of events. With each of the following features selecting a group will display a TableView component for further analysis.

#### Overview
This feature provides a breakdown of the API Events loaded. For each AWS Service a new panel is displayed which consists of the following information:

* The service name
* Number of API calls made by AWS (root) user : Blue section on left
* Number of API calls made by AWS IAM Users and Roles : Greenish section on right
* Events per second, minute and hour for each section
* Total number of API calls

##### Invoked By
The invoked by Feature shows a breakdown of the API calls that have been made separated into two groups, IAM Users and IAM Roles.

#### Error
Error Feature shows the API Error Codes that were thrown by the loaded events.

#### Security
The Security Feature shows events that might be considered to be Security Issues such as Security Groups being opened or IAM Policy Documents being modified.

You can modified what the application considers to be a Security Event using the Preferences Dialog.

#### Resources
The Resources Features shows events that related to resources being created or deleted, for example a EC2 being ran or a RDS being deleted.

You can modified what the application considers to be a Resource Event using the Preferences Dialog.

### GeoData
The GeoData Feature shows the locations (City) that the loaded Events has been actioned in. This only works with Events that have a valid IP as a SourceIpAddress. 

## Application Preferences
The Preferences dialog allows you to customise the application in some basic ways.

### AWS Accounts
The AWS Accounts section of the Preferences dialog is where you can enter one or more sets of AWS API Credentials. In order to read files from an S3 bucket you will need to add an entry here (see **Adding AWS API Credentials**).

One of the features of the Table View is the ability to view AWS Resource details, in order for that functionality to work you will need to create an entry for each AWS Account that you want to see details of. When creating an Account for that purpose you need to ensure that you enter the AWS Account Number.

As mentioned in the Table View section this functionality requires additional permissions over what is required to download files from S3.

### AWS Alias
An AWS Alias allows you to provide a friendly name for an AWS Account number. When you are browsing an S3 bucket the alias will be displayed instead of the account number. 

For example if you log you Dev, UAT and Prd CloudTrail events into the same bucket you might create an alias for each account.

This is optional, you only need to add an entry here if you are storing multiple Accounts worth of CloudTrial logs in the same bucket, it saves you having to remember what account number belongs to which account.

### Security
The security feature only shows those events that might be consider to a security risk. 

The application comes with some as an example, you can change that list to meet your requirements using this section.

### Resources
The resource feature only shows those events that are relevant to resource creation and deletion. 

The application comes with some as an example, you can change that list to meet your requirements using this section.

