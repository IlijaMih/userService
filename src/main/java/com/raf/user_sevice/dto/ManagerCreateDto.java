package com.raf.user_sevice.dto;

import javax.validation.constraints.NotBlank;

public class ManagerCreateDto extends UserCreateDto{

	@NotBlank
	private String hotelName;
	@NotBlank
	private String dateOfEmployment;
	
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getDateOfEmployment() {
		return dateOfEmployment;
	}
	public void setDateOfEmployment(String dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}
	
	
}
