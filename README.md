CloudTrail Viewer
================

Java desktop tool for viewing AWS CloudTrail log files

Overview
--------
CloudTrail Viewer is a desktop application written in Java that allows you to load
CloudTrail logs either from the local hard disk or from S3. Once the log files have
been loaded you can view the data, do searches and create charts.

Configuration
-------------
If you wish to load files directly from S3 then you have to rename the file found
in the config folder and add your AWS information to it.
The file needs to be renamed to **config.cfg** and you will need to provide the
name of the S3 bucket that contains your CloudTrail logs, and your AWS IAM User
API keys.

Usage
-----
To load files go the the Events menu and select to either load files from the local
machine or from S3. From the Events menu you can also clear the loaded events.

Querying the data
-----------------
Once you have loaded some data you have a number of options:

* Click the chart button on the toolbar to create a chart of the data. This 
functionality currently only does the Top 5 of whatever property you select. If you
are not interested in seeing the API calls made by AWS to monitor your account, 
then click the Ignore Root check box.

* If you click the Table icon on the toolbar a table will open that contains all
the events that have been loaded. If you click an event you will see the detail
either as a table or raw JSON on the right hand side. You can also create charts
from the data in this table by clicking the chart toolbar button on the window itself.

* You can enter a string into the search field and it will open a table that
contains all the Events that contain that string anywhere. Again you can create
a chart table, the chart will be based on the information in that table.

* On the Services menu you can get a chart that shows the sum of all events
for the top 5 AWS Services. On this menu you can also get a Transaction Per
Second chart that can be zoomed in.

* Finally on the toolbar there is a security icon, clicking this will prompt you where
to load files from. The files are loaded and only Events that match a specific
criteria will be loaded, those criteria : 
1. Any Events that have error codes
2. Any IAM actions that are considered a security risk such as Creating/Update/Deleting IAM objects.
3. Any action that is considered a network security risk such as VPN, VPC, ACL changes.

* You can scan through 100's of files quickly using this feature.
