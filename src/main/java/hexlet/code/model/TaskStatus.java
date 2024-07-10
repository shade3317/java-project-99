package hexlet.code.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@Table(name = "tasks_statuses")
@EntityListeners(AuditingEntityListener.class)
public class TaskStatus implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long       id;

    @Size(min = 1)
    @Column(unique = true)
    private String     name;

    @Size(min = 1)
    @Column(unique = true)
    private String     slug;

    @CreatedDate
    private LocalDate  createdAt;
}
