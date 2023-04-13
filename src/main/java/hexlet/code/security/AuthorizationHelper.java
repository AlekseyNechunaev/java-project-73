package hexlet.code.security;

import hexlet.code.entity.Task;
import hexlet.code.entity.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public AuthorizationHelper(UserRepository userRepository,
                               TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public boolean canAccessUser(Long idFromRequest) {
        Long idFromAuthenticate = getUserByAuthenticationName().getId();
        return idFromAuthenticate.equals(idFromRequest);
    }

    public boolean canAccessDeleteTask(Long taskId) {
        Long idFromAuthenticate = getUserByAuthenticationName().getId();
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return true;
        }
        return idFromAuthenticate.equals(task.getAuthor().getId());
    }

    private User getUserByAuthenticationName() {
        String email = getUserNameFromAuthentication();
        return userRepository.findByEmail(email).get();
    }

    private String getUserNameFromAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
