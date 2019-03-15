package testing.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "domain")
public class User implements Serializable {

    @Id
    private String FirstName;
    private String SecondName;

    public User(String FirstName, String SecondName) {
        this.FirstName = FirstName;
        this.SecondName = SecondName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getSecondName() {
        return SecondName;
    }
}
