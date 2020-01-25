package com.danisestan.checkmate.TestAutomation;

import com.danisestan.checkmate.Utils.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.HttpCommandExecutor;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.danisestan.checkmate.Utils.Resources.*;

class SessionRecord {

    static void recordBrowserSessionData() throws IOException {
        System.out.println("recordBrowserSessionData---------------------------");
        System.out.println(sessionIdFilePath);
        System.out.println(pidFilePath);
        System.out.println(urlFilePath);
        System.out.println(windowHandleFilePath);
        writeSessionID(Resources.driver.getSessionId().toString());
        HttpCommandExecutor executor = (HttpCommandExecutor) Resources.driver.getCommandExecutor();
        URL urlAddress = executor.getAddressOfRemoteServer();
        writeURL(urlAddress);
        writePID(urlAddress);
        writeWindowHandleId();
    }

    /**
     *     TODO  include Browser info on test results page
     *     include message when needed: if session extends across multiple tests, and user selects
     *     different browsers -- testing
     *     continues on original browser.
     *
     */

    private static void writePID(URL currentURL) throws IOException {
        String  inetAddressStr = InetAddress.getLoopbackAddress().toString();
        String loopback = inetAddressStr.substring(inetAddressStr.lastIndexOf("localhost/"));
        String urlString = currentURL.toString();
        String port = urlString.substring(urlString.lastIndexOf(":") + 1);
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        BufferedReader stdInput;
        String s = null;
        String pidStr = "";
        String driverExtension = null;

        
        System.out.println("INET: " + inetAddressStr);
        System.out.println("LOOPBACK : " + loopback);
        System.out.println("logging URL STRING: " + urlString);
        System.out.println("logging PORT: " + port);

        if (OS.equalsIgnoreCase("LINUX")) {
            String[] cmd = { "/bin/sh", "-c", "ps aux | grep " + processname + " | grep " + port };
            proc = rt.exec(cmd);
            
        } else if (OS.equalsIgnoreCase("WINDOWS")) {
            switch (browser.toUpperCase()) {
                case "CHROME":
                    proc = rt.exec("cmd /c echo off & (for /f \"tokens=5\" %a in ('netstat -aon ^| findstr " +  port + "') do tasklist /NH /FI \"PID eq %a\") & echo on" );
                    break;
                case "FIREFOX":
                    proc = rt.exec("cmd /c echo off & (for /f \"tokens=5\" %a in ('netstat -aon ^| findstr " +  port + "') do tasklist /NH /FI \"PID eq %a\") & echo on" );
                    break;
                case "EDGE":
                    proc = rt.exec("cmd /c echo off & tasklist /nh /fi \"imagename eq MicrosoftWebDriver.exe\" & echo on" );
                    break;
                default:
                    break;
            }
        }

        stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        s = stdInput.readLine().toUpperCase();
        
        System.out.println("DRIVER, PROCESS INFO: " + s);
        System.out.println("PROCESSNAME : " + processname);
        
        if (OS.equalsIgnoreCase("LINUX")) {
            String[] strArr = s.split("\\s+");
            pidStr = strArr[1];
        }
        else if (OS.equalsIgnoreCase("WINDOWS")) 
            pidStr = StringUtils.substringBetween(s, processname.toUpperCase(), "CONSOLE");
        
        pidStr = pidStr.trim();

        try (PrintWriter writer = new PrintWriter(pidFilePath)) {
            writer.println(pidStr);
            System.out.println("PID written to file successfully");
        }
    }

    private static void writeURL(URL address) {
        File f = new File(urlFilePath);
        System.out.println(urlFilePath);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(address);
            System.out.println("URL serialized successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void writeSessionID(String sessionIdString)  {
        final File file = new File(sessionIdFilePath);
        try {
            FileUtils.writeStringToFile(file, sessionIdString, StandardCharsets.UTF_8);
            System.out.println("SessionId written to file successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void writeWindowHandleId() {
       String windowHandleId = driver.getWindowHandle();
        try (PrintWriter writer = new PrintWriter(windowHandleFilePath)) {
            writer.println(windowHandleId);
            System.out.println("WindowHandle ID written to file successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public  static URL retrieveURL() throws IOException {
        URL originalURL = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(urlFilePath))) {
            originalURL = (URL) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return originalURL;
    }

    static String  retrieveSessionId() throws IOException {
        File f = new File(sessionIdFilePath);
        if(f.createNewFile()) 
            System.out.println("Creating session record: " + sessionIdFilePath);
        
        try {
            return new String(Files.readAllBytes(Paths.get(sessionIdFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

//        String fileContents = "";
//        File file = new File(sessionIdFilePath);
//        try {
//             fileContents = FileUtils.readFileToString(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new SessionId (fileContents);
    }

    public static String retrieveWindowHandle() throws IOException {
        return new String(Files.readAllBytes(Paths.get(windowHandleFilePath))).trim();
    }

    public static String retrievePID() throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(pidFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean sessionRecordExists(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }
}