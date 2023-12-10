package com.em.taskmanager.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Immutable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table(name="users")
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;

}
