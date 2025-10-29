package com.golance.backend.dto;

public class RechargeRequestDto {

	private Long userId;
	
	private int rechargeAmount;

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(int rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	
	
	
}
