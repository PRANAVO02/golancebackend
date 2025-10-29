package com.golance.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golance.backend.dto.CreditTransactionDto;
import com.golance.backend.model.CreditTransaction;
import com.golance.backend.model.Wallet;
import com.golance.backend.repository.CreditTransactionRepository;

@Service
public class CreditTransactionService {

    @Autowired
    private WalletService walletService;


    @Autowired
    private CreditTransactionRepository creditTransactionRepository;


    public List<CreditTransactionDto> getTransactionsForUser(Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        List<CreditTransaction> transactions = creditTransactionRepository.findByWallet(wallet);

        return
                transactions.stream()
                        .map(tx -> new CreditTransactionDto (
                                tx.getWallet().getUser().getId(),
                                tx.getAmount(),
                                tx.getType(),
                                tx.getDescription(),
                                tx.getTimestamp()
                        ))
                        .toList();

    }





}