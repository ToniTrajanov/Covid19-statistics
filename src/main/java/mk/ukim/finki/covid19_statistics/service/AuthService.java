package mk.ukim.finki.covid19_statistics.service;

import mk.ukim.finki.covid19_statistics.model.User;

public interface AuthService {
    User login(String username, String password);
}