package com.danisestan.checkmate.TestAutomation;

import com.danisestan.checkmate.TestAutomation.SessionRecord;
import com.danisestan.checkmate.Utils.Resources;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.JsonHttpCommandCodec;
import org.openqa.selenium.remote.http.JsonHttpResponseCodec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;

public class Driver extends RemoteWebDriver {
    private static Long pid = (long) -1;
    
//    hacky, but worky. Might not even be necessary, but removal would first require testing in Windows OS.
    public static RemoteWebDriver rwd;
    static {
        try {
            if(Resources.continueSession.equalsIgnoreCase("Y")) {
                rwd = attachedDriver();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static RemoteWebDriver attachedDriver() throws IOException {
        if (!SessionRecord.retrieveSessionId().isEmpty()) {
            final SessionId sessionId = new SessionId(SessionRecord.retrieveSessionId());;
            URL command_executor = SessionRecord.retrieveURL();
            
            CommandExecutor executor = new HttpCommandExecutor(command_executor) {
                @Override
                public Response execute(Command command) throws IOException {
                    Response response = null;
                    if (command.getName() == "newSession") {
                        response = new Response();
                        response.setSessionId(sessionId.toString());
                        response.setStatus(0);
                        response.setValue(Collections.<String, String>emptyMap());

                        try {
                            Field commandCodec = null;
                            commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                            commandCodec.setAccessible(true);
                            commandCodec.set(this, new JsonHttpCommandCodec());

                            Field responseCodec = null;
                            responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                            responseCodec.setAccessible(true);
                            responseCodec.set(this, new JsonHttpResponseCodec());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        response = super.execute(command);
                    }
                    return response;
                }
            };
            return new RemoteWebDriver(executor, new DesiredCapabilities());
        } else {
            return null;        
        }
    }
    
    private static boolean existingSession() throws IOException {
        if (new File(Resources.pidFilePath).exists() && !SessionRecord.retrievePID().trim().isEmpty())
        {

            String pidStr = SessionRecord.retrievePID().trim();
            pid = Long.parseLong(pidStr);
            String processName = "";
            String driverExtension = "";
            Process proc = null;
            Runtime rt = Runtime.getRuntime();

            if (Resources.OS.equalsIgnoreCase("LINUX"))
                driverExtension = "";
            else if (Resources.OS.equalsIgnoreCase("WINDOWS"))
                driverExtension = ".exe";
            switch (Resources.browser.toUpperCase()) {
                    case "CHROME": 
                        processName = "chromedriver" + driverExtension;
                        break;
                    case "FIREFOX": 
                        processName = "geckodriver" + driverExtension;
                        break;
                    case "EDGE": 
                        processName = "MicrosoftWebDriver" + driverExtension;
                        break;
                    default:
                        break;
                }
//                
            if (Resources.OS.equalsIgnoreCase("WINDOWS")) {
                proc = rt.exec("cmd /c echo off & tasklist /NH /FI \"PID eq " + pid + "\" | findstr " +
                        processName
                        + "& echo on");    
            } else if (Resources.OS.equalsIgnoreCase("LINUX")) {
                String[] cmd = { "/bin/sh", "-c", "ps aux | grep " + processName + " | grep " + pid };
                proc = rt.exec(cmd);
            }
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String s = stdInput.readLine();
            
            if (s == null || s.isEmpty()) {
                    System.out.println(s + "Session is NOT running");
                    return false;
                }
                else {
                    Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
                    if (processHandle.isPresent()) {
                        ProcessHandle.Info processInfo = processHandle.get().info();
                        System.out.println("Process Handle: " + processInfo);
                    }
                    
                    Resources.activeSession = processHandle.isPresent() && processHandle.get().isAlive();
                    
                    if (Resources.activeSession) {
                        System.out.println("Session is running");
                        return true;
                    }
                    else {
                        System.out.println("Session is NOT running");
                        return false;
                    }
                }
                
        } else  {
            return false;
        }
    }

    public static void  createNewDriver() {
        switch (Resources.browser.toUpperCase()) {
            case "CHROME":
                Resources.driver = new ChromeDriver();
                break;
            case "FIREFOX":
                Resources.driver = new FirefoxDriver();
                break;
            case "EDGE":
                Resources.driver = new EdgeDriver();
                break;
            default:
                Resources.driver = new ChromeDriver();
                break;
        }
    }

    public static void createAttachedDriver() throws IOException {
        Boolean foo = true;
        if (existingSession()) {
//            driver = attachedDriver();
//            if (driver == null)
//                createNewDriver()
//            else {
////                fill with if (foo) statement
//            }
//        }
//        if (foo) {
            Resources.driver = attachedDriver();
            File f = new File (Resources.windowHandleFilePath);
            if (f.exists())
            {
                try {
//                    String win = "CDwindow-(33762291A9CD9917628131BC933516D9)";
                    System.out.println("window handle stored: " + SessionRecord.retrieveWindowHandle());
                    System.out.println("window handle: " + Resources.driver.getWindowHandle());
                    System.out.println("####retrieving handle");
                    Resources.driver.switchTo().window(SessionRecord.retrieveWindowHandle());
                    System.out.println("####retrieving handle2");
                }
                catch (Exception e) {
                    System.out.println("####could not retrieve handle");
                    createNewDriver();
                }
            }
        }
        else {
            createNewDriver();
        }
    }

//    public static void killActiveSession() throws IOException {
//        //SessionId activeSID = Resources.driver.getSessionId();
//        String activeURL = Resources.driver.getCurrentUrl();
//        String activePort = activeURL.substring(18);
//        Runtime rt = Runtime.getRuntime();
//        Process proc = rt.exec("cmd /c netstat -ano | findstr " + activePort);


    //        BufferedReader stdInput = new BufferedReader(new  InputStreamReader(proc.getInputStream()));
//        String s = null;
//        String pidStr = "";
//        long pid = -1;
//        if ((s = stdInput.readLine()) != null) {
//            int index = s.lastIndexOf(" ");
//            pidStr = s.substring(index, s.length());
//            pidStr = pidStr.trim();
//            pid = Long.parseLong(pidStr);
//            System.out.println("Check if ending chromedriver while keeping window open...");
//        }
//        Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
//        activeSession = processHandle.isPresent() && processHandle.get().isAlive();
//        if (activeSession) {
//            System.out.println("Killing ChromeDriver session...");
//            System.out.println("TestPrint--> SESSION );
//            rt.exec("cmd /c Taskkill /PID" +pidStr+" /T /F");
//            System.out.println("... Session ended successfully.");
//
//        }
//        else {
//            System.out.println("Session no longer exists.");
//        }
//    }

    /*TODO kill drivers not attached to an active/running browser.
    * check all drivers processes with id not matching the current driver pid
    * check if the drivers are attached to active/running browser w/window handl
    * if browser is inactive, exit driver.
    *  #for ie -- all other driver instances must be closed.
*/
    static void closeInactiveDrivers() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process proc;
        String continueSessionCheck;
        String tcName;
        String browserToClose;
        String browserProcess = "";
        String inactiveSessionPIDToClose;
        String driverExtension = "";
        System.out.println("Closing inactive webdrivers...");
        for (int TC = 2; TC <= Resources.tcTotalNum + 1; TC++) {
            tcName = Resources.SuiteData.getCellData("TestCases", "TCID", TC);
            continueSessionCheck = Resources.SuiteData.getCellData("TestCases", "ContinueSession", TC);
            browserToClose = Resources.SuiteData.getCellData("TestCases", "Browser", TC);
            Resources.OS = Resources.SuiteData.getCellData("TestCases", "OS", TC);

            if (Resources.OS.equalsIgnoreCase("LINUX"))
                driverExtension = "";
            else if (Resources.OS.equalsIgnoreCase("WINDOWS"))
                driverExtension = ".exe";

            switch (browserToClose.toUpperCase()) {
                case "CHROME": 
                    browserProcess = "chromedriver" + driverExtension;
                    break;
                case "FIREFOX": 
                    browserProcess = "geckodriver" + driverExtension;
                    break;
                case "EDGE": 
                    browserProcess = "MicrosoftWebDriver" + driverExtension;
                    break;
                default:
            }
            
            inactiveSessionPIDToClose = System.getProperty("user.dir") +
                    "//src//main//java//com//danisestan//checkmate//SessionData//PID//" + tcName  + ".txt";
            if (!continueSessionCheck.equalsIgnoreCase("Y") && !browserToClose.equalsIgnoreCase("IE")) {
                proc = rt.exec("cmd /c & taskkill /f /fi \"imagename eq " + browserProcess + " /fi \"pid eq "
                        +  inactiveSessionPIDToClose + "\"");
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                System.out.println("echo response: " + stdInput.readLine());
//
//        for(HashMap<String, String> tc : retainSession) {
//            for (Map.Entry<String,String> closeSession : tc.entrySet()) {
//                String endProcess = closeSession.getKey();
//                String closeBrowserType = closeSession.getValue();
//                proc = rt.exec("cmd /c & taskkill /f /fi \"imagename eq " + closeBrowserType + ".exe\" /fi \"pid eq "
//                        +  endProcess + "\"");
//          taskkill /f /fi "imagename eq chromedriver.exe" /fi "pid eq ##############"
//                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                System.out.println("STREAM: " + stdInput.readLine());
//                String s = null;
//                s = stdInput.readLine();
//                if ((s = stdInput.readLine()) == null) {}
            }
        }
    }
}
