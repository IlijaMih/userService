package com.raf.user_sevice.dto;

public class IncrementReservationCountDto {
	
	private Long userId;
	
	

	public IncrementReservationCountDto() {
		super();
	}

	public IncrementReservationCountDto(Long userId) {
		super();
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}
