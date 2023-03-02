package com.jonathanmanes.imagegenerator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "image_generator_user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private boolean isAllowed;
    private Integer quota;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "image_generator_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
