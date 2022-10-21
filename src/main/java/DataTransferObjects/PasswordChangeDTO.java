package DataTransferObjects;

import java.io.Serializable;

public class PasswordChangeDTO implements Serializable {
    private boolean status = false;
    private String name;
    private String oldPassword;
    private String newPassword;

    public PasswordChangeDTO(String name, String oldPassword, String newPassword) {
        this.name = name;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        status = true;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
