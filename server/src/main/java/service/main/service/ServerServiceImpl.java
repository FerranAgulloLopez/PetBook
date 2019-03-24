package service.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.main.entity.User;
import service.main.exception.NotFoundException;
import service.main.repositories.UserRepository;

import java.util.Optional;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

    @Autowired
    private UserRepository userRepository;

    public boolean ConfirmLogin(String email, String password) throws NotFoundException {
        //TODO check if the user has confirmed his email
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        return user.get().checkPassword(password);
    }

    public void RegisterUser(User input) {
        userRepository.save(input);
    }
}
