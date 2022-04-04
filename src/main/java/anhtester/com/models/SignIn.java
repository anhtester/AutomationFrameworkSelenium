package anhtester.com.models;

import anhtester.com.constants.FrameworkConstants;
import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import io.github.sskorol.data.Sheet;
import io.github.sskorol.data.Source;
import lombok.Data;

@Sheet(name = "SignIn")
@Data
public class SignIn {

    @RowNumber
    private int row;

    //@Column(name="Test Case Name",index = 0) ->
    //Column name is Test Case Name and it is at 0th index in Excel sheet
    @Column(name = "TESTCASENAME", index = 0)
    private String testCaseName;

    @Column(name = "EMAIL", index = 1)
    private String email;

    @Column(name = "PASSWORD", index = 2)
    private String password;

    @Column(name = "EXPECTED_TITLE", index = 3)
    private String expectedTitle;

    @Column(name = "EXPECTED_ERROR", index = 4)
    private String expectedError;

    @Column(name = "EXPECTED_URL", index = 5)
    private String expectedUrl;

    // converterClass = IntegerConverter.class -> This will do the conversion from String to int
    // @Column(name="age",index = 3, converterClass = IntegerConverter.class)
    //public int age;

    public int getRow() {
        return row;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }

    public String getExpectedError() {
        return expectedError;
    }

    public String getExpectedUrl() {
        return expectedUrl;
    }
}
