package az.dev.smallbankingapp.security;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Data
public class CustomAuthentication implements Authentication {

    private final Collection<? extends GrantedAuthority> authorities;
    private UserPrincipal userPrincipal;

    public CustomAuthentication(
            UserPrincipal userPrincipal,
            Collection<? extends GrantedAuthority> authorities) {
        this.userPrincipal = userPrincipal;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        //do nothing
    }

    @Override
    public String getName() {
        return null;
    }

}
