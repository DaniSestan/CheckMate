# CheckMate: Test Automation Framework
<img src="https://github.com/DaniSestan/CheckMate/raw/master/images/checkmate.png" alt="Checkmate" title="CheckMate" width="25%" height="25%" />

<br/>

CheckMate is an Java-based test automation framework using Selenium's automation tools. It's designed for web and REST API testing, implementing the following features: 

<br/>

* **Reusability:** the framwork is set apart by its ability to implement and maximize reusability. Scripts can be modified and allowed to run from any point in the test. This is particularly useful when troubleshooting failpoints. This is done by preserving the session data from the browser while running each test. The driver can then step "back in time", essentially starting from any point in a prior test instance, using the recorded session data. Tests are also modularized, allowing for scripts to be broken down into components which can reused in other test scripts where they are referenced.
* **Keyword-Driven Testing:** with keywords, no coding is required to write test scripts. Testers can add and customize keywords that symbolize the functionalities used in testing.
* **Data-Driven Testing:** allows users to easily create and manipulate large data sets and to test large volumes of data quickly.
* **Reporting:** the framework automatically generates reports and screenshots of the tests, and prints the results to an html file in an easy-to-read format. Reports provide details about each test step, including specifics about where, when, and how the step was executed and what test data was used.
* **Cross Browser/Platform:** Compatible with Windows and Linux and supports all major browsers.
* **Environments:** Build your tests once and run them against multiple environments or using different sets of test data.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Compatibility and Software Requirements:

**OS support**: Windows, Linux, support for macOS coming soon

**Browser support**: Chrome, Firefox, Edge

**Additional requirements**: Application for viewing and editing XLS spreadsheet files

### Configuration and Deployment


If you are using IntelliJ, the IDE should preserve the project structure once the source code has been cloned.

If for some reason, you are required to manually add framework-specific settings, or the necessary facets are not automatically detected, then you will need to include the configuration explained below. If using a different editor, such as Eclipse, then the setup process may differ, but the following information may still prove useful for managing the launch config.

<br/>

#### (In IntelliJ) Adding the web module and facet:

![Modules and Facets](https://github.com/DaniSestan/CheckMate/raw/master/images/Modules%20and%20Facets.png)

Go to 'Project Settings' -> 'Modules'. Add a module of type 'Web'. Similarly, go to 'Poject Settings -> Facets, and select the type  as as 'Web', then select 'CheckMate' as the module to which the web facet added. Once the module and facet have been added, the deployment descriptors, web resource directories, and source roots should be visible (as is shown below).
- - -
#### Artifacts:

Before launching the application, the artifact (JAR) is built, so this step is to be included in the run configuration (see section on 'run configuration' below).
- - -
#### Run configuration:

![Run Config](https://github.com/DaniSestan/CheckMate/raw/master/images/Run%20Config.png)

Main class: com.danisestan.checkmate.TestAutomation.TestExecution

Working directory: This should be the full path of the CheckMate repo you cloned locally.

Before launch:

* Run Maven Goal 'checkmate: clean'
* Build 'CheckMate:jar' artifact
* Build

## Test Case Run Configuration

Open the TestSuite.xlsx file in the 'Data' directory. The 'TestCases' sheet should contain 9 columns for running tests, each of which are required for running tests. Each row represents a separate test case in the test batch.

![Test Case Run Configuration](https://github.com/DaniSestan/CheckMate/raw/master/images/TestCases%20sheet.png)

<br/>

**RunMode:** Enter 'Y' to run the test case. Enter 'N' to skip the test case.

**TCID:** Identify the test case with a name, i.e. 'TC01'

**Description:** Provide a few words describing the test case, i.e. 'Login Page'

**Modularize:** If the test case implements one or more test modules, enter 'Y'. Otherwise, enter 'N'.
* What are test modules?
Test modules are not much different from the test cases themselves, in that they consist of a series of steps to be automated during testing. The difference is that the modules are reusable test components. An example scenario would be wanting to run 3 separate tests in a batch. Each test case will test a separate functionality of the web application. One case will test adding items to the cart. Another will test editing the user's account information. The final case will test reviewing past orders. Each test case requires the same series of steps used to automate the process of logging into the user's account (regardless of whether the data used in each test -- such as the login username and password -- needs to be different).
This series of steps is then added to a test module. Each test requiring steps from the module will then reference that module. (More information will be provided on how to create and refernce modules in a later section.)

**DefaultDataSet:** Enter the dataset to be used for testing. The same data set can be used for multiple test cases.

**OS:** You must select "Windows" or "Linux" depending on your OS.

**Browser:** Depending on the browser in which you wish to run the test case, select 'Chrome', 'Firefox', or 'Edge'

**ContinueSession:** Mark 'Y' if you wish to continue testing from a prior test session. Mark 'N' otherwise. Allowing users to continue testing where a prior test left off is meant to aid in the process of debugging.
* Continuing sessions:<br/>A test script will terminate once it reaches a fail point. If the tester wants the option of continuing test sessions, the browser window used for testing should reamain open after the test has ended. In its initial run, 'ContinueSession' can be marked as 'Y'. The 'KillSessionPostTest' column, however, must be marked as 'N' -- this permits the framework, once the test case has completed its run, to record information pertaining to the browser session and driver instance used during testing. The tester would then view the logs to review information including the test step, keyword, and the data at the fail point. The  script and/or data would then be modified and prior test steps would be marked to be be skipped (this is explained further in a later section). Prior to any subsequent runs, the tester needs to mark the test case as 'Y' in the 'ContinueSession' column. The test case can then be run using the modified script and/or data, starting from the point at which the prior test session ended. This process can be repeated as many times as required, provided that for each subsequent run, the test case must have the 'KillSessionPostTest' marked as 'N', and the 'ContinueSession' column marked 'Y'.

**KillSessionPostTest:** This will kill the driver process used to run the test. This should be marked as 'N' if the tester wishes to run continued sessions from this test case.

## Scripting Tests
The framework contains a excel sheet, 'TestSuite.xlsx' located in the directory 'src/main/java/com/danisestan/checkmate/Data/'. Sample tests are provided in the spreadsheet. The sample scripts can be used a template to be referenced when creating new new ones. Each script is divided into steps, and each step has its own attributes.

### Defining Test Modules

![TestModules sheet](https://github.com/DaniSestan/CheckMate/raw/master/images/TestModules%20Sheet.png)

All test modules need to be referenced in a sheet called 'TestModules'. In this sheet, there are two columns:

**TMID**: This column lists all test module IDs
 
**Description:** This column contains brief description of the test module in the 'Description' column.
- - -
### Defining Data Sets

![DataSets sheet](https://github.com/DaniSestan/CheckMate/raw/master/images/DataSets%20Sheet.png)

All datasets used need to be referenced in the 'DataSets' sheet, which contains two columns:

**DSID:** This column will list the IDs of each dataset.

**DataSet Description:** This column contains a brief description of each dataset.
- - -
### Modularized Test Script

![Modularized test script sheet](https://github.com/DaniSestan/CheckMate/raw/master/images/Modularized%20TestCase%20Sheet.png)

If a test case has its 'Modularize' column marked as 'Y' in the 'TestCases' sheet, then it must follow this format.
If you wish to split the test scripts into separate components (i.e, a set of steps for logging into the application, and another for editing the account profile, or whatever else), then you would later be able to use any of these components, or test modules, in any other test case, rather than having to manually rewrite all of the same steps in a new script.
Create a sheet titled with the exact string used to label the 'TCID' column. For instance, if the 'TestCase' sheet contains a test case with its 'Modularize' column marked 'Y' and its 'TCID' column marked 'TC01', then create a sheet titled 'TC01' and in the sheet create the following columns:


**TMID:** This column contains the ID of the test module.

**DSID:** This column contains the ID of the dataset used for the test module.

**Skip:** This column can be marked 'Y' if all the steps contained in the test module need to skipped in the test case. Otherwise, mark it 'N'.
- - -
### Non-Modularized Test Script

![Non-modularized test script](https://github.com/DaniSestan/CheckMate/raw/master/images/Non-Modularized%20TestCase%20Sheet.png)

If a test case has its 'Modularize' column marked as 'N' in the 'TestCases' sheet, the sheet containing the test script needs to follow this format.
Create a sheet titled with the exact string used to label the 'TCID' column. For instance, if the 'TestCase' sheet contains a test case with its 'Modularize' column marked 'N' and its 'TCID' column marked 'TC01', then create a sheet titled 'TC01' and in the sheet create the following columns:

**TSID:** This column contains the test step ID, which needs to be an integer starting with '1'. Each step is numbered sequentially, in ascending order (1, 2, 3, ...).

**Description:** This column contains a brief description of the step, i.e., 'Enter password'.

**Keyword:** This column contains the keyword that represents the function called for that step. Keywords are defined in the framework. Keywords for most commonly used functions are already provided by the framework.<br/><br/>_Custom keywords can be created in the java file and then referenced here also. Custom keywords are created using Selenium's library. Most of the keywords you would require are already provided, but creating custom keyword functions is not difficult, provided you have a basic understanding of Java. If not, the learning curve for what's required to create keywords is not a steep one. If you get stuck, the internet is your friend! There are numerous guides and forums online to help with this process._<br/>

All available keywords are contained in the following file: 'CheckMate/src/main/java/com/danisestan/checkmate/TestAutomation/Keywords.java'. This file contains all the functions that represent the keywords to be referenced here. The keyword method to be used is referenced here, and the name reference is case-sensitive.

**WebElement:** In this column, if the keyword used for the step performs an action on an element within the application, the element is referenced here. This value represents a variable that holds an xpath to the web element being referenced. All web elements are contained as key-value pairs in the file 'CheckMate/src/main/java/com/danisestan/checkmate/repository/createAnAccount.properties'.

**SkipStep:** If you wish to skip the step when executing the test case, mark 'Y', otherwise 'N'.

**ProceedOnFail:** If you require the test to attempt to continue executing subsequent steps if this step fails, mark 'Y'. This is applicable in some situations, whereas it can cause conflicts in other scenarios. You may not want to mark the column 'Y' if the test step is providing input for a field that is required to proceed to another point in the test scenario. This can be useful in other situations, such as testing the option to enter the correct input after attempting to enter an invalid input leading to a fail.

**ProceedOnPass:** Like 'ProceedOnPass', this column allows the tester to determine whether the test continues ('Y') or stops executing ('Y'). Here, the condition for proceeding is that the step passes.

**TestDataField:** Here, a test field requires some kind of input, this column references a value from the test set being used for the test case. Find the dataset used for test and go to the sheet containing the dataset ID in the file 'TestDataSuite.xlsx' file. This sheet in the TestDataSuite file should contain the column 'DSID'. This column should contain the ID corresponding to the dataset being used. (More information about navigating through the data spreadsheet is provided in a later section.) Find the column that contains the value needed for the step, i.e., a column titled 'password' which contains the passwords being entered during the login process of the application. Enter the column title, and note that the value entered is case-sensitive.<br/><br/>

## Creating and Manipulating Data Sets

Data sets are stored in the file 'TestSuiteData.xlsx' located in 'CheckMate/src/main/java/com/danisestan/checkmate/Data'. This section explains how to create format these data sets.

### Defining Data Sets

![Data - DataSets sheet](https://github.com/DaniSestan/CheckMate/raw/master/images/Data%20-%20DataSets%20Sheet.png)

This sheet contains three columns:

**DSID:** First, reference the 'TestSuite.xlsx' file, and navigate to the 'TestCases' sheet. Find the values in the 'DefaultDataSet' column. Each of these values lists the ID for the data set being used in each test case. Each of these ID values should also be listed in the 'DSID' column of the DataSets sheet located in the 'TestSuiteData.xlsx' file. Each DSID should only be referenced once, regardless of whether it is used in multiple test cases.
- - -
**Sheet:** All of the values stored in a data set are listed in a sheet in this 'TestSuiteData.xlsx' file. Each sheet can contain multiple data sets. How you determine what data sets should be stored in what sheets is up to you. You can categorize and store data sets together based on what part of the application the data is used for, or based on the different test modules that use them, or based on any other categorization. The title of the sheet that stores the data set needs to be referenced here (this value is case-sensitive).
- - -
**Description:** Here a description of the data set can be listed.

### Adding the Data

![data sheet](https://github.com/DaniSestan/CheckMate/raw/master/images/Data%20-%20Data%20sheet.png)

Each sheet containing data sets must contain the following columns:

**DSID:** This column contains the 'DSID', that is, the ID of the data set.

**Remaining columns:** The remaining columns are each labeled with whatever field the input value should store. For instance, you may have a column titled 'waitfor', and each data set row would store an integer representing how many seconds the waitfor keyword will use to delay test execution. Another column labeled 'username' may list all the different usernames used to login to the application. The test scripts in the 'TestSuite.xlsx' file will reference this column label in the scripts themselves, in each scripts 'TestDataField' column.

<br/>

## Built With

Some of the key tools and technologies used to build this application:

* [Maven](https://maven.apache.org/) - dependency management
* [Java (version 11.0.4)](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) - Project SDK
* Webdrivers:
	* [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/) , [GeckoDriver](https://github.com/mozilla/geckodriver/releases), [MicrosoftWebDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
* [Selenium](https://selenium.dev/) - provides the automation tools used for running tests in the web browser

* [Apache POI](https://poi.apache.org/) - reading script data from .xlsl files.
* [Apache Log4J](https://logging.apache.org/log4j/2.x/) - logging librarry for Java

<br/>

## Versioning

This project uses [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/DaniSestan/CheckMate/tags).

<br/>

## Authors

* **Dani Sestan** - *Initial work* - [DaniSestan](https://github.com/DaniSestan)

<br/>

## License

This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License. To view a copy of this license, see the [LICENSE.md](https://github.com/DaniSestan/CheckMate/blob/master/LICENSE.md) file.
