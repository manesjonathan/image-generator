package com.jonathanmanes.imagegenerator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "image_generator_role")
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}

