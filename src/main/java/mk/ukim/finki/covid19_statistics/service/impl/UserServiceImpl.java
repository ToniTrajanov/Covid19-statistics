package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.enumerations.Role;
import mk.ukim.finki.covid19_statistics.model.User;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.covid19_statistics.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.covid19_statistics.model.exceptions.UserRoleNotFoundException;
import mk.ukim.finki.covid19_statistics.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.repository.UserRepository;
import mk.ukim.finki.covid19_statistics.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(s));
    }


    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, Role userRole) {
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUserCredentialException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        if(userRole == null)
            throw new UserRoleNotFoundException();
        User user = new User(username,passwordEncoder.encode(password),name,surname,userRole);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
