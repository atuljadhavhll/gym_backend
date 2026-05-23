package com.gymwalabackend.entity;

import jakarta.persistence.*;
import com.gymwalabackend.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String fullName;
    private String gender;
    @Column(nullable=false, unique=true)
    private String email;
    private String provider;
    private String password;
    private String pictureUrl;
    private String role_type;



    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;


}
