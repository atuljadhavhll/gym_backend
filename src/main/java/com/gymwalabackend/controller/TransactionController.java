package com.gymwalabackend.controller;

import com.gymwalabackend.entity.Transaction;
import com.gymwalabackend.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{memberId}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long memberId,
            @RequestParam Double amount) {
        Transaction tx = transactionService.saveTransaction(memberId, amount);
        return ResponseEntity.ok(tx);
    }
}
