package service.main.entity.input_output.user;

public class OutLogin {

    private boolean success;
    private boolean mailconfirmed;
    private String jwtToken;

    public OutLogin(boolean success, boolean mailconfirmed, String jwtToken) {
        this.success = success;
        this.mailconfirmed = mailconfirmed;
        this.jwtToken = jwtToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isMailconfirmed() {
        return mailconfirmed;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
