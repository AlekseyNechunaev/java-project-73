package hexlet.code.repository;

import hexlet.code.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Task> findAllByAuthorId(Long authorId);

    List<Task> findAllByExecutorId(Long executorId);

    List<Task> findAllByTaskStatusId(Long id);
}
