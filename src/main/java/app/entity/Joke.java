package app.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class Joke extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    @CreationTimestamp
    public LocalDateTime created_at;

    @NotBlank
    public String joke;
}

