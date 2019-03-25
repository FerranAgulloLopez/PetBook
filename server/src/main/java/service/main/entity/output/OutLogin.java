package service.main.entity.output;

public class OutLogin {

    private boolean success;
    private boolean mailconfirmed;

    public OutLogin(boolean success, boolean mailconfirmed) {
        this.success = success;
        this.mailconfirmed = mailconfirmed;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isMailconfirmed() {
        return mailconfirmed;
    }
}
