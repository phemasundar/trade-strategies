
import com.strategies.trade.utilities.CustomLogStrings;
import com.strategies.trade.utilities.CustomLogging;
import org.testng.Assert;

/**
 * @author hemasundarpenugonda
 */
public class CustomAssertions {

    public static void assertText(String act, String exp) {
        assertText(act, exp, "");
    }

    public static void assertText(String act, String exp, String message) {
        try {
            Assert.assertEquals(act, exp, message);
            CustomLogging.writeLog("Assertion Passed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);

        } catch (Exception e) {
            CustomLogging.writeLog("Assertion Failed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);
            throw e;
        }
    }

    public static void assertInteger(Integer act, Integer exp, String message) {
        try {
            Assert.assertEquals(act, exp, message);
            CustomLogging.writeLog("Assertion Passed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);

        } catch (Exception e) {
            CustomLogging.writeLog("Assertion Failed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);
            throw e;
        }
    }

    public static void assertInteger(Integer act, Integer exp) {
        assertInteger(act, exp, "");
    }

    public static void assertNotText(String act, String exp, String message) {
        try {
            Assert.assertNotEquals(act, exp, message);
            CustomLogging.writeLog("Assertion Passed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);

        } catch (Exception e) {
            CustomLogging.writeLog("Assertion Failed | " + message + ":\n\tExpected: " + exp + "\n\tActual: " + act);
            throw e;
        }
    }

    public static void assertTrue(boolean act) {
        assertTrue(act, "");
    }

    public static void assertTrue(boolean act, String message) {
        try {
            Assert.assertTrue(act, message);
            CustomLogging.writeLog("Assertion Passed | " + message);

        } catch (Exception e) {
            CustomLogging.writeLog("Assertion Failed | " + message);
            throw e;
        }
    }

    public static void assertFalse(boolean act) {
        assertFalse(act, "");
    }

    public static void assertFalse(boolean act, String message) {
        try {
            Assert.assertFalse(act, message);
            CustomLogging.writeLog("Assertion Passed | " + message);

        } catch (Exception e) {
            CustomLogging.writeLog("Assertion Failed | " + message);
            throw e;
        }
    }

    @Deprecated
    public static void assertResponseCodeAs200(int responseCode, String message) {
        if (responseCode == 200) {
            CustomLogging.writeLog(CustomLogStrings.PASSED + message + " : 200");
        } else {
            CustomLogging.writeLog(CustomLogStrings.FAILED + message + " : " + responseCode);
            throw new AssertionError("ASSERTION FAILED: Expected response code: 200, Actual response code: " + responseCode);
        }

    }
}
