package com.golance.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golance.backend.dto.RechargeRequestDto;
import com.golance.backend.dto.TransferRequestDto;
import com.golance.backend.model.CreditTransaction;
import com.golance.backend.model.User;
import com.golance.backend.model.Wallet;
import com.golance.backend.repository.CreditTransactionRepository;
import com.golance.backend.repository.UserRepository;
import com.golance.backend.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditTransactionRepository creditTransactionRepository;

    public Wallet getWallet(User user) {
        return walletRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Wallet not found"));
    }

    public void recharge(RechargeRequestDto rechargeRequestDto) {
        Long userId = rechargeRequestDto.getUserId();
        int amount = rechargeRequestDto.getRechargeAmount();

        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance() + amount);

        walletRepository.save(wallet);

        //saving the transaction details(recharges details)
        saveWalletTransaction(wallet, amount, "RECHARGE", "RECHARGE_SELF");

    }

    public int getBalanceByUserId(Long userId) {

        return getWalletByUserId(userId).getBalance();
    }


    @Transactional
    public Wallet getWalletByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(user);
                    newWallet.setBalance(0);
                    // Save immediately so it's no longer transient
                    return walletRepository.save(newWallet);
                });

        return wallet;
    }

    public void saveWalletTransaction(Wallet wallet, int amount, String type, String description) {
        CreditTransaction transaction = new CreditTransaction(); //this is the entity

        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);

        creditTransactionRepository.save(transaction);
    }

    @Transactional
    public void transferCredit(TransferRequestDto transferRequestDto) {
        Long fromUserId = transferRequestDto.getFromUserId();
        Long toUserId = transferRequestDto.getToUserId();
        int transferAmount = transferRequestDto.getAmount();  //no of credits.

        Wallet fromUserWallet = getWalletByUserId(fromUserId);
        Wallet toUserWallet = getWalletByUserId(toUserId);

        if(fromUserWallet.getBalance()<transferAmount)
            throw new RuntimeException("Insufficient credits");

        //transferring credits
        fromUserWallet.setBalance(fromUserWallet.getBalance() - transferAmount);
        toUserWallet.setBalance(toUserWallet.getBalance() + transferAmount);

        walletRepository.save(fromUserWallet);
        walletRepository.save(toUserWallet);



        //saving the transaction.
        String fromUsername = userRepository.findById(fromUserId).orElseThrow(()->new RuntimeException("User not found")).getUsername();
        String toUsername = userRepository.findById(toUserId).orElseThrow(()->new RuntimeException("User not found")).getUsername();
        saveWalletTransaction(fromUserWallet, -transferAmount, "TRANSFER", "TRANSFER TO " + toUsername);
        saveWalletTransaction(toUserWallet, transferAmount, "TRANSFER", "TRANSFER FROM " + fromUsername);

    }

}