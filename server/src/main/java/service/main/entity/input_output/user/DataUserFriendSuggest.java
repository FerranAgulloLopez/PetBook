package service.main.entity.input_output.user;

import java.io.Serializable;

public class DataUserFriendSuggest implements Serializable {

    private String firstName;
    private String secondName;
    private String foto;

    /*
    Get
     */

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFoto() { return foto; }

    public DataUserFriendSuggest() {
    }

    public DataUserFriendSuggest(String firstName, String secondName, String foto) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.foto = foto;
    }
}
