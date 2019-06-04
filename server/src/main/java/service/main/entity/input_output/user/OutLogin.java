package service.main.entity.input_output.user;

public class OutLogin {

    private boolean success;
    private boolean mailconfirmed;
    private boolean admin;

    public OutLogin(boolean success, boolean mailconfirmed, boolean admin) {
        this.success = success;
        this.mailconfirmed = mailconfirmed;
        this.admin = admin;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isMailconfirmed() {
        return mailconfirmed;
    }

    public boolean isAdmin() {
        return admin;
    }
}
