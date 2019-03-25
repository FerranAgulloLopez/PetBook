package service.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.main.entity.User;
import service.main.entity.output.OutLogin;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;
import service.main.repositories.UserRepository;
import service.main.util.EmailServiceImpl;

import java.util.Optional;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

    @Autowired
    private UserRepository userRepository;

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        boolean result = user.get().checkPassword(password);
        return new OutLogin(result,user.get().isMailconfirmed());
    }

    public void RegisterUser(User input) {
        //TODO check if input has email and password
        userRepository.save(input);
    }
    public void ConfirmEmail(String email) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        user.get().setMailconfirmed(true);
        userRepository.save(user.get());
    }

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        if (user.get().isMailconfirmed()) throw new BadRequestException("The user has already verified his email.");
        sendEmail();
    }

    private void sendEmail() {
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage("fewrna@gmail.com", "yea", "Just testing my friend");
    }


}
