package service.main.entity.input_output.user;

public class DataUpdatePassword {

    private String oldPassword;
    private String newPassword;

    public DataUpdatePassword () {}

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
