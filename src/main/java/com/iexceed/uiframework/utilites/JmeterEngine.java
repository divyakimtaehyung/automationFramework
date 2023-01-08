package com.iexceed.uiframework.utilites;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.*;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.AuthPanel;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import static com.iexceed.uiframework.utilites.FileDirectoryUtil.flushDirectory;

public class JmeterEngine {
    private static Logger LOGGER = LogManager.getLogger(JmeterEngine.class);
    private static StandardJMeterEngine jMeterEngine;
    protected static HashTree testplanTree;
    private static File testplanFile;
    private static Summariser summer;
    protected static HTTPSamplerProxy httpSamplerProxy;
    private static ReportGenerator reportGenerator;
    private static CookieManager cookieManager;
    private static LoopController loopController;
    private static AuthManager authManager;
    protected static ThreadGroup threadGroup;
    protected static TestPlan testPlan;
    private static HeaderManager headerManager;
    private static String reportDirectory = "target/performancereport/";
    private static File jmeterHomePath;
    private static HashTree threadGroupHashTree;
    private static ResponseAssertion responseAssertion;



    static {
        jMeterEngine = new StandardJMeterEngine();
    }

    private void JMeterEngine() {

    }

    //This method will load and Identify JMeter in your system.
    public static void loadJMeterconfig(){
        jmeterHomePath = null;
        testplanTree = new HashTree();
        jmeterHomePath= new File("/home/i-exceed.com/ankita.guttedar/apache-jmeter-5.3");
       // jmeterHomePath = new File(System.getProperty("jmeter.home", System.getenv("JMETER_HOME")));
        if (jmeterHomePath.exists()) {
            JMeterUtils.setJMeterHome(jmeterHomePath.getPath());
            JMeterUtils.loadJMeterProperties(jmeterHomePath.getPath() + File.separator + "bin" + File.separator + "jmeter.properties");
            JMeterUtils.initLocale();
            LOGGER.info("JMeter Home Initialized successfully");
        } else {
            LOGGER.info("JMeter Home not set, existing from execution");
            System.exit(1);
        }
    }

    public static void setAuthentication(String url, String username, String password) {
        authManager = new AuthManager();
        Authorization authorization = new Authorization();
        authorization.setURL(url);
        authorization.setUser(username);
        authorization.setPass(password);
        authManager.addAuth(authorization);
        LOGGER.info("Initalized Authentication Manager");
        authManager.setName(JMeterUtils.getResString("Authorization Manger")); // $NON-NLS-1$
        authManager.setProperty(TestElement.TEST_CLASS, AuthManager.class.getName());
        authManager.setProperty(TestElement.GUI_CLASS, AuthPanel.class.getName());
    }

    public static HeaderManager setHeaderManager() {
        headerManager = new HeaderManager();
        headerManager.setName("Header Manager");
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return headerManager;
    }

    public static void setResponseAssertion(String value) {
        responseAssertion = new ResponseAssertion();
        responseAssertion.setTestFieldResponseCode();
        responseAssertion.addTestString(value);
        responseAssertion.isTestFieldResponseCode();
        responseAssertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
        responseAssertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
        responseAssertion.isTestFieldResponseCode();

    }

    public static HTTPArgument getHttpArgument() {
        return new HTTPArgument();
    }


    //HTTP Sampler with port
    public static void setHttpSampler(String protocolType, String setDomainName, int setPort, String setPath, String requestType) {
        httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain(setDomainName);
        httpSamplerProxy.setPort(setPort);
        httpSamplerProxy.setPath(setPath);
        httpSamplerProxy.setMethod(requestType);
        httpSamplerProxy.setProtocol(protocolType);
        httpSamplerProxy.setName("Http Sampler");
        LOGGER.info("Initalized Http Sampler");
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
    }

    //HTTP Sampler without port
    public static void setHttpSampler(String protocolType, String setDomainName, String setPath, String requestType) {
        httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain(setDomainName);
        httpSamplerProxy.setPath(setPath);
        httpSamplerProxy.setMethod(requestType);
        httpSamplerProxy.setProtocol(protocolType);
        httpSamplerProxy.setName("Http Sampler");
        LOGGER.info("Initalized Http Sampler");
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
    }


    //Loop Controller
    public static void setLoopController(int loopCount) {
        loopController = new LoopController();
        loopController.setLoops(loopCount);
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();
        LOGGER.info("Initalized Loop Controller");
    }

    //Loop Controller
    public static void setThreadGroup(int noOfThreads, int setRamupNo) {
        threadGroup = new ThreadGroup();
        threadGroup.setName("Thread Group");
        threadGroup.setNumThreads(noOfThreads);
        threadGroup.setRampUp(setRamupNo);
        threadGroup.setSamplerController(loopController);
        LOGGER.info("Initialized Thread Group");
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
    }

    //Cookies Manager
    public static void setCookieManager() {
        cookieManager = new CookieManager();
        cookieManager.setName("Cookie Manager");
        LOGGER.info("Initialized Cookie Manager");
        cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
        cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());
    }

    //Creating Test Plan
    public static void initializeTestPlan(String testPlanName) {
        testPlan = new TestPlan(testPlanName);
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
    }

    public static HashTree configureTestPlan() {
        testplanTree.add("TestPlan", testPlan);
        testplanTree.add("loopController", loopController);
        testplanTree.add("ThreadGroup", threadGroup);
        testplanTree.add("HTTPSamplerProxy", httpSamplerProxy);
        return testplanTree;
    }

    public static void saveTestPlan(String filePath, String fileName) throws IOException {
        SaveService.saveTree(testplanTree, new FileOutputStream(filePath + fileName));
    }

    public static void executeTestWithJMXFile(String reportName, String jmxFilePath) {
        try {
            testplanFile = new File(System.getProperty("testPlan.location", jmxFilePath));
            SaveService.loadProperties();
            testplanTree = SaveService.loadTree(testplanFile);
            LOGGER.info("Run and Generate Report");
            generateReport(reportName);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            System.exit(1);
        }
    }

    public static void generateReport(String reportName) {
        try {
            JMeterUtils.setProperty("jmeter.reportgenerator.exporter.html.property.output_dir", reportDirectory + reportName);
            String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
            if (summariserName.length() > 0) {
                summer = new Summariser(summariserName);
            }
            File report = new File(reportDirectory);
            File reportFile = new File(reportDirectory + reportName  + "result.jtl");
            if (report.exists()) {
                LOGGER.info("Report folder yet to be deleted");
                flushDirectory(report);
                LOGGER.info("Report folder deleted");
                if (reportFile.exists()) {
                    boolean delete1 = reportFile.delete();
                    LOGGER.info("Report File deleted" + delete1);
                }
            }

            ResultCollector logger = new ResultCollector(summer);
            reportGenerator = new ReportGenerator(reportFile.getPath(), logger);
            logger.setFilename(reportFile.getPath());
            testplanTree.add(testplanTree.getArray()[0], logger);
            jMeterEngine.configure(testplanTree);
            LOGGER.info("Performance Execution Started..........");
            jMeterEngine.run();
            reportGenerator.generate();
            LOGGER.info("Report Generated Successfully");
            System.exit(0);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            System.exit(1);
        }
    }
}
