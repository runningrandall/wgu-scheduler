# Scheduler Application

## Purpose

The purpose of this application is to provide customer, contact, and appointment management.
The application allows users to login, view/edit customers, view/edit appointments, and run various reports.

## Meta Information

#### Author

Randall Adams


#### Contact Information

* Phone: 801-210-0858
* Email: randall.adams@outlook.com

#### Application Version

Current application version is 1.0.0 as found in the pom.

#### Date

Updated as of 12/27/2021

#### Application Build Details

* IDE: `IntelliJ Community 2020.2.4`
* JDK: `AdoptOpenJDK-11.0.11+9`
* JavaFX: `JavaFX-SDK-11.0.2`
* MySQL Connector Driver: `8.0.2.1`

## Running The Application

1. clone the repo
1. Create a `src/main/resources/application.properties` with the following contents:
```
db.host=jdbc:mysql://YOUR_DB_HOST
db.username=SOME_USERNAME
db.password=SOME_PASSWORD
db.schema=client_schedule
```
1. Run a `mvn clean install`
1. Add a run `Application" and name it `Main`. Include the following configuration details:
    * Main Class: `com.randalladams.scheduler.MainApp`
    * VM Options: `--module-path "C:\Program Files\Java\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml`

## Reports

The following reports are available:

1. Report appointment types
    * Prints appointment counts by type
2. Report contact schedule
    * Prints a schedule by contact name
3. Login activity report
    * Prints all the login activity for each user including the date and status
    
