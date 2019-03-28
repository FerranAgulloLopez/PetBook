package service.main.entity.output;

public class OutUpdateUserProfile {

    private String FirstName;
    private String SecondName;
    private String dateOfBirth;
    private String postalCode;


    public OutUpdateUserProfile() {}

    public String getFirstName() {
        return this.FirstName;
    }

    public String getSecondName() {
        return this.SecondName;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getPostalCode() {
        return  this.postalCode;
    }


}
