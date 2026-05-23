package com.gymwalabackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setAmount(Double amount) {
    }

    public void setDate(LocalDateTime now) {
    }

    public void setMember(Member member) {
    }
}
