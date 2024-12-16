package com.orange.devrap.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Joke extends PanacheEntity {

    public LocalDateTime created_at = LocalDateTime.now();
    public String joke;
}
