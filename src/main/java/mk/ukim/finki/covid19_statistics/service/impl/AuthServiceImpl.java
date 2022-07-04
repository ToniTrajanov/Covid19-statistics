package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.User;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.covid19_statistics.repository.UserRepository;
import mk.ukim.finki.covid19_statistics.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new InvalidArgumentException();
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialException::new);
    }

}