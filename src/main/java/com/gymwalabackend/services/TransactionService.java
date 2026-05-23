package com.gymwalabackend.services;

import com.gymwalabackend.entity.Member;
import com.gymwalabackend.entity.Transaction;
import com.gymwalabackend.repository.MemberRepository;
import com.gymwalabackend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Transaction saveTransaction(Long memberId, Double amount) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        Transaction tx = new Transaction();
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setMember(member);

        return transactionRepository.save(tx);
    }
}
