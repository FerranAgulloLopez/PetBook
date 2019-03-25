package service.main.service;

import service.main.entity.User;
import service.main.entity.output.OutLogin;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;

public interface ServerService {

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException;

    public void RegisterUser(User input);
}
