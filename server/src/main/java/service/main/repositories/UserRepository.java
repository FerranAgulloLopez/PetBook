package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.User;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {

    public List<User> findByPostalCode(String postalcode);
    public List<User> findByPostalCodeLike(String postalcode);



    public List<User> findByFirstName(String firstname);
    public List<User> findByFirstNameLike(String firstname);


    public List<User> findByPostalCodeAndFirstName(String postalcode, String firstname);
    public List<User> findByPostalCodeAndFirstNameLike(String postalcode, String firstname);



}
