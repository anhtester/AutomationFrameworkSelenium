package anhtester.com.models;

public class LoginModel {
    private String email;
    private String password;

    public LoginModel(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public LoginModel() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
