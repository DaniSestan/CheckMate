package com.danisestan.checkmate.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.danisestan.checkmate.Data.Xls_Reader;
import org.openqa.selenium.remote.RemoteWebDriver;

//import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

public class Resources {
	public static boolean activeSession;
	protected static boolean killActiveSessionPT;
	protected static int DSIDrow = -1;
	protected static int DSIDsheetRow = -1;
	protected static int ModuleTotal = -1;
	protected static Properties AppText = new Properties();
	protected static Properties Repository = new Properties();
	public static String continueSession = "";
	public static RemoteWebDriver driver;
	protected static String keyword = "";
	protected static String webElement = "";
	public static String TestCaseID = "";
	protected static String TestDataField = "";
	protected static String TestData = "";
	protected static String ProceedOnFail = "";
	protected static String ProceedOnPass = "";
    protected static String skipStep= "";
	protected static String TSID = "";
	protected static String Description = "";
	public static String OS = "";
	public static String driverExtension = "";
	public static String browser = "";
	public static String processname = "";
	public static Xls_Reader SuiteData;
	protected static Xls_Reader TestStepData;
	protected static String tsDetails = "";
	public  static int tcTotalNum = -1;
//	public static String processname = "";
//	public static ArrayList<HashMap<String, String>> retainSession = new ArrayList<>();
    public static int tcIndex = -1;
    public static String urlFilePath  = System.getProperty("user.dir") +
            "//src//main//java//com//danisestan//checkmate//SessionData//URL//" + TestCaseID  + ".txt";
    public static String pidFilePath = System.getProperty("user.dir") +
            "//src//main//java//com//danisestan//checkmate//SessionData//PID//" + TestCaseID  + ".txt";
    public static String sessionIdFilePath = System.getProperty("user.dir") +
            "//src//main//java//com//danisestan//checkmate//SessionData//SessionId//"  + TestCaseID  + ".txt";
    public static String windowHandleFilePath = System.getProperty("user.dir") +
            "//src//main//java//com//danisestan//checkmate//SessionData//WindowHandle//"  + TestCaseID  + ".txt";

    protected  static void setSessionIdFilePath(String testCaseIdentifier){
        sessionIdFilePath = System.getProperty("user.dir") +
                "//src//main//java//com//danisestan//checkmate//SessionData//SessionId//"  + TestCaseID  + ".txt";
    }
    protected  static void setPidFilePath(String testCaseIdentifier){
        pidFilePath = System.getProperty("user.dir") +
                "//src//main//java//com//danisestan//checkmate//SessionData//PID//"  + TestCaseID  + ".txt";
    }
    protected  static void setUrlFilePath(String testCaseIdentifier){
        urlFilePath = System.getProperty("user.dir") +
                "//src//main//java//com//danisestan//checkmate//SessionData//URL//"  + TestCaseID  + ".txt";
    }
    protected  static void setWindowHandleFilePath(String testCaseIdentifier){
        windowHandleFilePath= System.getProperty("user.dir") +
                "//src//main//java//com//danisestan//checkmate//SessionData//WindowHandle//"  + TestCaseID  + ".txt";
    }
    protected static void Initialize() throws IOException {
		System.out.println("INITIALIZING");
		FileInputStream FI ;
		TestStepData = new Xls_Reader(System.getProperty("user.dir")+"//src//main//java//com//danisestan//checkmate//Data//TestSuiteData.xlsx");
		SuiteData = new Xls_Reader(System.getProperty("user.dir")+"//src//main//java//com//danisestan//checkmate//Data//TestSuite.xlsx");
		File xpathProperties_loginPage = new File(System.getProperty("user.dir") +
				"//src//main//java//com//danisestan//checkmate//repository//login.properties");
		File xpathProperties_createAccount = new File(System.getProperty("user.dir")
				+"//src//main//java//com//danisestan//checkmate//repository//createAnAccount.properties");

		FI = new FileInputStream(xpathProperties_loginPage);
		Repository.load(FI);
		FI = new FileInputStream(xpathProperties_createAccount);
		Repository.load(FI);
	}
}
