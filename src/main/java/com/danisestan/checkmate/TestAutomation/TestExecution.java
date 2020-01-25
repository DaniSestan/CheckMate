package com.danisestan.checkmate.TestAutomation;

import com.danisestan.checkmate.ReportUtils.ReportUtil;
import com.danisestan.checkmate.TestAutomation.Keywords;
import com.danisestan.checkmate.Utils.Resources;
import com.danisestan.checkmate.Utils.TestUtils;
import com.danisestan.checkmate.Data.Xls_Reader;
import org.openqa.selenium.NoSuchElementException;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//import static com.daniasestan.checkmate.TestAutomation.Driver.closeInactiveDrivers;
import static com.danisestan.checkmate.TestAutomation.Driver.createAttachedDriver;
import static com.danisestan.checkmate.TestAutomation.Driver.createNewDriver;
import static com.danisestan.checkmate.TestAutomation.SessionRecord.recordBrowserSessionData;

public class TestExecution extends Resources {

    private static ArrayList<Integer> DSIDrowList;// = new ArrayList<>();
    private static ArrayList<String> DataSetList;// = new ArrayList<>();
    private static ArrayList<String> DataSetSheetList;// = new ArrayList<>();
    private static ArrayList<String> TestModuleList;// = new ArrayList<>();;
    private static boolean endedOnFail;
    private static String DataSetID = "";
    public static String DataSetSheet = "";
    private static String SkipModule = "";
    private static String TestModuleID = "";
    private static String TestModule = "";
//    private static String ContinueSession = "";
    private static String killSessionPostTest = "";
    private static String TCStatus = "";
    private static String TSStatus = "";
    private static String RunMode = "";
    private static String modularize = "";
    private static String defaultDataSet = "";
    
    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchElementException, IOException {
        System.out.println("USER DIRECTORY--> " + System.getProperty("user" +
                ".dir"));
        Initialize();
        String startTime = TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa");
        ReportUtil.startTesting(System.getProperty("user.dir") + "//src//main//java//com//danisestan//checkmate//Reports//index.html", startTime, "TestAutomation", "1.5");
        ReportUtil.startSuite("Suite1");

        // loop through the test cases
        tcTotalNum = SuiteData.getRowCountMinusHeader("TestCases");
        System.out.println("TESTCASE TOTAL : " + tcTotalNum);
        //InactiveDrivers();
//        if (browser.equalsIgnoreCase("IE")){
//            System.out.println("Ending MicrosoftWebDriver process...");
//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec("cmd /c & taskkill /f /im chrome.exe");

//        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        String s = stdInput.readLine();
//        System.out.println("echo response: " + s);
//        }
        
        for (int TC = 2; TC <= tcTotalNum + 1; TC++) {
            tcIndex = TC;
            System.out.println("TESTCASE" + TC);
            DSIDrowList = new ArrayList<>();
            DataSetList = new ArrayList<>();
            DataSetSheetList = new ArrayList<>();
            TestModuleList = new ArrayList<>();
            TestCaseID = SuiteData.getCellData("TestCases", "TCID", TC);
            RunMode = SuiteData.getCellData("TestCases", "RunMode", TC);
            modularize = "";
            
            if (RunMode.equalsIgnoreCase("Y")) {
                modularize = SuiteData.getCellData("TestCases", "Modularize",
                        TC);
                defaultDataSet = SuiteData.getCellData("TestCases",
                        "DefaultDataSet", TC);
                continueSession = SuiteData.getCellData("TestCases", "ContinueSession", TC);
                killSessionPostTest = SuiteData.getCellData("TestCases", "KillSessionPostTest", TC);
                OS = SuiteData.getCellData("TestCases", "OS", TC);
                browser = SuiteData.getCellData("TestCases", "Browser", TC);
                TCStatus = "Pass";
                SkipModule = "";

                if (OS.equalsIgnoreCase("LINUX"))
                    driverExtension = "";
                else if (OS.equalsIgnoreCase("WINDOWS"))
                    driverExtension = ".exe";

                switch (browser.toUpperCase()) {
                    case "CHROME":
                        processname = "chromedriver" + driverExtension;
                        break;
                    case "FIREFOX":
                        processname = "geckodriver" + driverExtension;
                        break;
                    case "EDGE":
                        processname = "MicrosoftWebDriver" + driverExtension;
                        break;
                    default:
                        break;
                }
                
                System.out.println("Executing Test Case: " + SuiteData.getCellData("TestCases", "TCID", TC));
                
//                switch (OS.toUpperCase()) {
//                    case "LINUX": 
//                        driverExtension = "";
//                        break;
//                    case "WINDOWS": 
//                        driverExtension = ".exe";
//                        break;
////                    case "MACOS": 
////                        break;
//                    default:
//                        driverExtension = ".exe";
//                        break;
//                }
                
//                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//Drivers//ChromeDriver//chromedriver" + driverExtension);
//                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//Drivers//GeckoDriver//geckodriver" + driverExtension);
//                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "//Drivers//MicrosoftWebDriver//MicrosoftWebDriver" + driverExtension);
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//Drivers//ChromeDriver//" + processname);
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//Drivers//GeckoDriver//" + processname);
                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "//Drivers//MicrosoftWebDriver//" + processname);
                
                if (killSessionPostTest.equalsIgnoreCase("Y")) {
                    killActiveSessionPT = true;
                }
                setSessionIdFilePath(TestCaseID);
                setPidFilePath(TestCaseID);
                setUrlFilePath(TestCaseID);
                setWindowHandleFilePath(TestCaseID);
                if (continueSession.equalsIgnoreCase("N")) {
                    createNewDriver();
                } else if (continueSession.equalsIgnoreCase("Y")
                        && new File(sessionIdFilePath).exists() && new File(pidFilePath).exists()
                        && new File(urlFilePath).exists() && new File(windowHandleFilePath).exists()) {
                    createAttachedDriver();
                }

                if (modularize.equalsIgnoreCase("N")) {
                    TestModuleID = TestCaseID;
                    TestModuleList.add(TestModuleID);
                    DataSetID = defaultDataSet;
                    DataSetList.add(DataSetID);
                    DSIDsheetRow = TestStepData.getCellRowNum("DataSets", "DSID", DataSetID);
                    DataSetSheet = TestStepData.getCellData("DataSets", "Sheet", DSIDsheetRow);
                    DataSetSheetList.add(DataSetSheet);
                    DSIDrow = TestStepData.getCellRowNum(DataSetSheet, "DSID", DataSetID);
                    DSIDrowList.add(DSIDrow);
                    System.out.println("Test Case: " + TestCaseID
                            + "\t\\Single script Component: " + TestModuleID +
                            "\t\\Data Set: " + DataSetID);
                } else {
                    // loop through the test modules
                    System.out.println(TestCaseID);
                    for (int TM = 2; TM <= SuiteData.indexColumnSize(TestCaseID, "TMID"); TM++) {
                        SkipModule = SuiteData.getCellData(TestCaseID, "Skip", TM);
                        if (!SkipModule.equalsIgnoreCase("Y")) {
                            TestModuleID = SuiteData.getCellData(TestCaseID, "TMID", TM).toUpperCase();
                            TestModuleList.add(TestModuleID);
                            DataSetID = SuiteData.getCellData(TestCaseID, "DSID", TM).toUpperCase();
                            if (DataSetID.isEmpty() || DataSetID == null) {
                                DataSetID = defaultDataSet;
                            }
                            DataSetList.add(DataSetID);
                            DSIDsheetRow = TestStepData.getCellRowNum("DataSets", "DSID", DataSetID);
                            DataSetSheet = TestStepData.getCellData("DataSets", "Sheet", DSIDsheetRow);
                            DataSetSheetList.add(DataSetSheet);
                            DSIDrow = TestStepData.getCellRowNum(DataSetSheet, "DSID", DataSetID);
                            DSIDrowList.add(DSIDrow);
                            System.out.println("Test Case: " + TestCaseID +
                                    "\t\\Test Modules: " + TestModuleID +
                                    "\t\\tData Set: " + DataSetID);
                        }
                    }
                }

                ModuleTotal = TestModuleList.size();
                System.out.println("Total mods : " + ModuleTotal);
                
                for (int TM = 1; TM <= ModuleTotal ; TM++) {
                    TestModule = TestModuleList.get(TM - 1);
                    DataSetSheet = DataSetSheetList.get(TM - 1);
                    DSIDrow = DSIDrowList.get(TM - 1);
                    System.out.println("Executing Test Case: " + TestCaseID +
                            "\t\\TestModuleID:" + TestModule);
                    for (int TS = 2; TS <= SuiteData.indexColumnSize(TestModule, "TSID"); TS++) {
                        System.out.println("ROWS MINUS HEADER: " + SuiteData.getRowCountMinusHeader
                                (TestModule));
                        TSID = SuiteData.getCellData(TestModule, "TSID", TS);
                        Description = SuiteData.getCellData(TestModule, "Description", TS);
                        keyword = SuiteData.getCellData(TestModule, "Keyword", TS);
                        webElement = SuiteData.getCellData(TestModule, "WebElement", TS);
                        TestDataField = SuiteData.getCellData(TestModule, "TestDataField", TS);
                        TestData = TestStepData.getCellData(DataSetSheet, TestDataField, DSIDrow);
                        ProceedOnFail = SuiteData.getCellData(TestModule, "ProceedOnFail", TS);
                        ProceedOnPass = SuiteData.getCellData(TestModule, "ProceedOnPass", TS);
                        skipStep = SuiteData.getCellData(TestModule,
                                "SkipStep", TS);

                        Method method = Keywords.class.getMethod(keyword);
                        if (skipStep.equalsIgnoreCase("N") || skipStep.isEmpty()) {
                            try{
                                TSStatus = (String) method.invoke(method);
                            } catch (Exception e) {
                                TSStatus = "Failed";
                                throw (e);
                            }
                        }
                        else {
                            TSStatus = "Skipped Step";
                        }
                            
                        switch (TSStatus) {
                            case "Pass":
                                System.out.println("Step passed");
                                if (!ProceedOnPass.isEmpty() && Xls_Reader.isInteger(ProceedOnPass)) {
                                    System.out.println("Proceed on Pass to StepID: " + TS);
                                    TS = Xls_Reader.stringCellToInt(ProceedOnPass);
                                }
                                ReportUtil.addKeyword(TSID, Description,
                                        keyword, TSStatus, tsDetails, null);
                                break;
                            case "Failed":
                                System.out.println("Failed at step");
                                // take the screenshot
                                String filename = java.time.LocalDateTime.now() + " - " +  TestCaseID + "[" + (TC - 1)
                                        + "]" + TestModule + "." + "TS" + TSID;
                                TestUtils.getScreenShot(filename);
                                TCStatus = "Failed";
                                // report error
                                ReportUtil.addKeyword(TSID, Description,
                                        keyword, TSStatus, tsDetails, "Screenshot/" + filename + ".jpg");
                                if (ProceedOnFail.isEmpty() || !Xls_Reader.isInteger(ProceedOnFail)) {
                                    System.out.println(ProceedOnFail + " IS NOT INTEGER");
                                    endedOnFail = true;
                                    break;
                                } else {
                                    System.out.println("Proceed on Fail to StepID: " + TS);
                                    TS = Xls_Reader.stringCellToInt(ProceedOnFail);
                                }
                                break;
                            case "Skipped Step":
                                System.out.println("Skipped step, proceeding to StepID: " + TS + 1);
                                ReportUtil.addKeyword(TSID, Description,
                                    keyword, TSStatus, tsDetails, null);
                                break;
                            default: ReportUtil.addKeyword(TSID, Description,
                                    keyword, TSStatus, tsDetails, null);
                                break;
                        }
                        
                        System.out.print("END OF TEST STEP");
                        if (endedOnFail) {
                            break;
                        }
                    }
                }
                
                ReportUtil.addTestCase(TestCaseID, startTime, TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"), TCStatus);
                System.out.println("Current URL: " + driver.getCurrentUrl());
                recordBrowserSessionData();
                if (killActiveSessionPT) {
                    driver.quit();
                }
                if (browser.equalsIgnoreCase("IE")){
                    System.out.println("Ending MicrosoftWebDriver process...");
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec("cmd /c echo off & taskkill /f /im MicrosoftWebDriver.exe  & echo on");
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String s = null;
                    s = stdInput.readLine();
                    System.out.println("echo response: " + s);
                }
            } else {
                // skip the test case
                System.out.println("Skipped testcase: " + TestCaseID);
                ReportUtil.addTestCase(TestCaseID, startTime, TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"), "Skipped");
            }
        }
        ReportUtil.endSuite();
        ReportUtil.updateEndTime(TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"));
    }
}


