package mk.ukim.finki.covid19_statistics.model.enumerations;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
