package com.golance.backend.controller;

import com.golance.backend.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golance.backend.dto.RechargeRequestDto;
import com.golance.backend.dto.TransferRequestDto;
import com.golance.backend.model.User;
import com.golance.backend.service.UserService;
import com.golance.backend.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private WalletService walletService;

	//read balance
	@GetMapping("/balance/{userId}")
	public int getBalance(@PathVariable Long userId) {
		User user = userService.getUserById(userId);

		// getWalletByUserId already creates wallet if missing
		Wallet wallet = walletService.getWalletByUserId(userId);

		return wallet.getBalance();
	}


	//update balance - since post is not idempotent we use POST here, which is required for recharge
	//multiple recharge requests should update the balance
	
	@PostMapping("/recharge")
	public void recharge(@RequestBody RechargeRequestDto rechargeRequestDto) {
		walletService.recharge(rechargeRequestDto);
	}
	
	@PostMapping("/transfer")
	public void transferCredit(@RequestBody TransferRequestDto transferRequestDto) {
		
		walletService.transferCredit(transferRequestDto);
		
	}
}
