package hexlet.code.security;

import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationHelper {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthenticationHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canAccess(Long idFromRequest) {
        Long idFromAuthenticate = getUserByAuthenticationName().getId();
        return idFromAuthenticate.equals(idFromRequest);
    }

    private User getUserByAuthenticationName() {
        String email = getUserNameFromAuthentication();
        return userRepository.findByEmail(email).get();
    }

    private String getUserNameFromAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
