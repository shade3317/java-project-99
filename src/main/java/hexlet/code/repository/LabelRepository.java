package hexlet.code.repository;
import hexlet.code.model.Label;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label>      findByName(String name);
    Optional<Set<Label>> findByIdIn(Set<Long> ids);
}
