package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;
import mk.ukim.finki.covid19_statistics.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name="Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String surname;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public String getNameAndSurname() {
        return name+ " "+surname;
    }

    public User(String username, String password, String name, String surname, Role userRole) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = userRole    ;
    }


    public User() { }

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNotExpired = true;
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}