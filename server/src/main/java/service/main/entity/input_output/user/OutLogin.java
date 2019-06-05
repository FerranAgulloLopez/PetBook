package service.main.entity.input_output.user;

public class OutLogin {

    private boolean success;
    private boolean mailconfirmed;
    private boolean admin;
    private boolean banned;

    public OutLogin(boolean success, boolean mailconfirmed, boolean admin, boolean banned) {
        this.success = success;
        this.mailconfirmed = mailconfirmed;
        this.admin = admin;
        this.banned = banned;
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

    public boolean isBanned() {
        return banned;
    }
}
