CloudTrail Viewer
================

Java desktop tool for viewing AWS CloudTrail log files


NOTE : This application required Java 8 at this time due to a dependency on controlsfx


Current functionality includes loading files from the local machine and from S3. If you wish to load files from S3
then you will need to rename the file config_example.cfg to config.cfg and provide your AWS API credentials and the
name of the bucket in whic the CloudTrail files exist.
