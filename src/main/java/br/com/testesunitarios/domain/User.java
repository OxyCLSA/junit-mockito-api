package br.com.testesunitarios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(min = 2, max = 100)
    private String name;


    @Column(unique = true)
    private String email;


    @Size(min = 6, max = 100)
    private String password;
}
