package com.golance.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.golance.backend.model.CreditTransaction;
import com.golance.backend.model.Wallet;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    List<CreditTransaction> findByWallet(Wallet wallet);

}