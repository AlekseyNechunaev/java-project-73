package hexlet.code.security;

import hexlet.code.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class CustomUserDetails implements UserDetails {

    private static final List<? extends GrantedAuthority> AUTHORITIES = Collections.emptyList();
    private static final boolean ACCOUNT_IS_NON_EXPIRED = true;
    private static final boolean ACCOUNT_IS_NON_LOCKED = true;
    private static final boolean CREDENTIALS_IS_NON_EXPIRED = true;
    private static final boolean IS_ENABLED = true;

    private final User user;

    protected CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return ACCOUNT_IS_NON_EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ACCOUNT_IS_NON_LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CREDENTIALS_IS_NON_EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return IS_ENABLED;
    }
}
