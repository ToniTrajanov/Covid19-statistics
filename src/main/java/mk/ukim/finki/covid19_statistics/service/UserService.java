package mk.ukim.finki.covid19_statistics.service;

import mk.ukim.finki.covid19_statistics.model.enumerations.Role;
import mk.ukim.finki.covid19_statistics.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.*;
public interface UserService extends UserDetailsService {


    User register(String username, String password, String repeatPassword, String name, String surname, Role role);
    List<User> findAll();
}
