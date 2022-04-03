package anhtester.com.utils;

import anhtester.com.constants.FrameworkConstants;
import anhtester.com.models.SignIn;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.TestDataReader;
import io.github.sskorol.data.XlsxReader;
import one.util.streamex.StreamEx;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public final class DataProviderUtils {

    /**
     * Private constructor to avoid external instantiation
     */
    private DataProviderUtils() {
    }

//	private static List<Map<String, String>> list =	new ArrayList<>();

//	@DataProvider(parallel=false)
//	public static Object[] getData(Method m) {
//		String testname = m.getName();
//
//		if(list.isEmpty()) {
//			list = ExcelUtils.getTestDetails("");
//			System.out.println(list);
//		}
//		List<Map<String, String>> smalllist = new ArrayList<>(list);
//
//		Predicate<Map<String,String>> isTestNameNotMatching = map ->!map.get("testname").equalsIgnoreCase(testname);
//		Predicate<Map<String,String>> isExecuteColumnNo = map -> map.get("execute").equalsIgnoreCase("no");
//
//		smalllist.removeIf(isTestNameNotMatching.or(isExecuteColumnNo));
//		return smalllist.toArray();
//
//	}

    @Test(dataProvider = "getLoginDataSupplier")
    public void test1(SignIn testData) {
        System.out.println("-----------------------------");
        System.out.println("testData.testCaseName = " + testData.getTestCaseName());
        System.out.println("testData.username = " + testData.getEmail());
        System.out.println("testData.password = " + testData.getPassword());
        System.out.println("testData.expectedTitle = " + testData.getExpectedTitle());
        System.out.println("testData.expectedError = " + testData.getExpectedError());
    }

    //@DataProvider --> Return type -> Object[][] or Object[]
    //@DataSupplier //--> It can read any file (CSV, xlsx, JSON, YAMLDataSupplierTest)
    //@DataSupplier(runInParallel = true)
    @DataSupplier(runInParallel = true, name = "getSignInDataSupplier")
    public StreamEx<SignIn> getData(Method method) {

        return TestDataReader.use(XlsxReader.class)
                .withTarget(SignIn.class)
                //By default, it looks for files in src/test/resources directory
                .withSource(FrameworkConstants.EXCEL_DATA_PATH)
                .read()
                //  .filter(testData -> testData.getTestCaseName().equalsIgnoreCase("titleValidationTest"));

                // Using Java reflection -> Getting method name and use it for filtering
                .filter(testData -> testData.getTestCaseName().equalsIgnoreCase(method.getName()));
    }

    @DataProvider(name = "loginDataTest")
    public static Object[][] loginDataTest() {
        return new Object[][]{
                //Email, Password, Expected Error Message
                {"admin02@mailinator.com", "123456", "Invalid credentials"}
        };
    }

}
