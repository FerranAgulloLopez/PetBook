package service.main.service;

import service.main.entity.User;
import service.main.exception.NotFoundException;

public interface ServerService {

    public boolean ConfirmLogin(String email, String password) throws NotFoundException;

    public void RegisterUser(User input);
}
