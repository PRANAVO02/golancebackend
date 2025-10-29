package com.golance.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golance.backend.dto.CreditTransactionDto;
import com.golance.backend.service.CreditTransactionService;

@RestController
@RequestMapping("/api/transactions")
public class CreditTransactionController {
	
	@Autowired
	private CreditTransactionService creditTransactionService;

	@GetMapping("/{userId}")
	public List<CreditTransactionDto> getTransactions(@PathVariable Long userId) {
		return creditTransactionService.getTransactionsForUser(userId);
	}
}
