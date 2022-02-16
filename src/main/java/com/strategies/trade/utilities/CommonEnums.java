package com.strategies.trade.utilities;

interface EnvironmentData {
    //Doesn't require for the current app
    @Deprecated
    String getAuthorization();

    String getBaseURL();

    String getTestDataPath();

}

/**
 * @author hemasundarpenugonda
 */
public class CommonEnums {
    public enum OperatingSystem {
        IOS, ANDROID, MAC;
    }

    public enum BrowserType {
        CHROME, FIREFOX, NATIVE;
    }

    public enum TestExecutionEnvironment {
        LOCAL, SAUCE;
    }

    public enum Device {
        MOBILE, DESKTOP;
    }

    public enum Locators {
        ID, NAME, CLASSNAME, CSS, XPATH, LINKTEXT, ACCESSIBILITY, ANDROID_UI_AUTOMATOR;
    }

}

