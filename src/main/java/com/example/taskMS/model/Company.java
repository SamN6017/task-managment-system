package com.example.taskMS.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // One company has many users, projects, etc.
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> employees;
}