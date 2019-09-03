package org.ga4gh.RefgetTestSuite;

import org.ga4gh.ComplianceFramework.Constants;
import org.ga4gh.RefgetUtilities.RefgetSession;
import org.ga4gh.RefgetUtilities.RefgetUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RefgetBaseTest {

    @BeforeTest
    public void setup() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(Constants.RESULT_DIR + "results.json"));
        RefgetSession.resultsArray = (JSONArray) ((JSONObject)object).get("servers");
        RefgetSession.setupEnvironment(System.getProperty("url"));
    }

    @AfterTest
    public void dumpJSON(ITestContext context) throws IOException {
        RefgetSession.testObject.put("total_tests", context.getAllTestMethods().length);
        RefgetSession.testObject.put("total_passed_tests", context.getPassedTests().size());
        RefgetSession.testObject.put("total_failed_tests", context.getFailedTests().size());
        RefgetSession.testObject.put("total_skipped_tests", context.getSkippedTests().size());

        if(!RefgetUtilities.replaceIfResultPresent(RefgetSession.testObject, RefgetSession.resultsArray)) {
            RefgetSession.resultsArray.add(RefgetSession.testObject);
        }

        FileWriter file = new FileWriter(Constants.RESULT_DIR + "results.json");
        JSONObject object = new JSONObject();
        object.put("servers", RefgetSession.resultsArray);
        file.write(object.toJSONString());
        file.flush();
    }
}
