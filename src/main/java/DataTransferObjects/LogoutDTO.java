package DataTransferObjects;

import java.io.Serializable;

public class LogoutDTO implements Serializable {
    private boolean status = false;
    private String name;

    public LogoutDTO(String name) {
        this.status = true;
        this.name = name;
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
}
