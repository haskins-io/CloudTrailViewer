## CloudTrail viewer : Users guide

CloudTrail viewer is a Java application for reading and analysing AWS CloudTrial log files.

The log files can be loaded from the local filesystem or from an AWS S3 bucket.

### Preferences ###
The Preferences option on the File menu is where you can configure the application.

#### AWS Accounts
In this section you can defined AWS accounts to work with. This functionality is only required to load files from an S3
bucket. If you are not intending to do this then you can leave this section.

If you are intending to load files directly from S3 then you need to provide the following information:

+ S3 bucket name

Then either:

+ profile : If you have configured your /aws/credentials file : [AWS Docs](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html)

or

+ API Key
+ API Secret

#### Security
The security section allows you to define what AWS API calls you consider to be a security risk. The application comes
with some values pre-loaded. The events defined here are use by the Security Widget.

You can remove existing values if you wish, or add another. When you click the add button to add another you need to type
in the name of the API call that you consider to be a risk.

#### Resources
The resources section works similar to the Security Section. It allows you to define what AWS API calls you want to monitor.
The idea of this section is that you define API events that create, modify or delete resources.
 
The application comes with some values pre-loaded. The entries defined here are use by the Resources Widget.

You can remove existing values if you wish, or add another. When you click the add button to add another you need to type
in the name of the API call that you want to monitor.

### Events
The Events menu is where you will import CloudTrail Log files into the application. There are two ways of doing this.

#### Local Events
You can load files you have downloaded and have on your local machine.

#### Remote Events
You can load files from an S3 bucket. _See AWS Account section above_ 

### Dashboards
A dashboard is a collection of widgets that show information about loaded CloudTrail events.

When you first run the application you should see the default dashboard. This dashboard contains the following widgets.

+ Map : Showing which cities API calls have been made from
+ Pie Chart : Showing the top 5 API Events that have been made
+ Stacked Bar Chart : Showing top events per AWS Service
+ Bar Chart: Top 5 API Events
+ Table : Top 5 Events.

At the bottom of the screen is a table that will show events that have been loaded. This table is populated in different
ways. 

#### Charts
Clicking on a pie segment or a bar in a chart will populate the Event Table with the events that where used to populate
that section of the chart.

#### Tables
Clicking on a row in the table will populate the Event Table with the events that where used to populate that 
section of the chart.

#### Creating a dashboard
To create a new dashboard choose new from the Dashboard menu. A dialog will open asking for the name of the dashboard.

#### Loading a dashboard
To load a dashboard select open on the Dashboard menu. A dialog will open showing you all available dashboard (expect
the one that is currently open).

#### Saving a dashboard
To save a dashboard select save on the Dashboard menu. The dashboard will be save, over writing any existing dashboard
with the same name

#### Adding a widget
To add a widget to a Dashboard select add widget from the Dashboard menu. A dialog will open for you to select the type
of widget too added. Once you have selected a widget another dialog will open allowing you to configure the widget.

#### Removing a widget.
To remove a widget click the remove button on the widget. This only removes the widget from the current view. If you
need to permanently remove the widget you will need to save the dashboard as described above.

#### Modifying a widget.
You can change what a widget is showing by clicking on the edit button. This will present a dialog where you can change
the widget.