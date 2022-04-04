package anhtester.com.models;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import io.github.sskorol.data.Sheet;
import lombok.Data;

@Sheet(name = "Client")
@Data
public class Client {

    @RowNumber
    private int row;

    @Column(name = "TESTCASENAME", index = 0)
    private String testCaseName;

    @Column(name = "COMPANY_NAME", index = 1)
    private String companyName;

    @Column(name = "OWNER", index = 2)
    private String owner;

    @Column(name = "ADDRESS", index = 3)
    private String address;

    @Column(name = "CITY", index = 4)
    private String city;

    @Column(name = "STATE", index = 5)
    private String state;

    @Column(name = "ZIP", index = 6)
    private String zip;

    @Column(name = "COUNTRY", index = 7)
    private String country;

    @Column(name = "PHONE", index = 8)
    private String phone;

    @Column(name = "WEBSITE", index = 9)
    private String website;

    @Column(name = "VAT", index = 10)
    private Integer vat;

    @Column(name = "CLIENT_GROUP", index = 11)
    private String clientGroup;

    @Column(name = "STATUS", index = 12)
    private String status;

    public int getRow() {
        return row;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getStatus() {
        return status;
    }
}
