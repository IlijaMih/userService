package com.raf.user_sevice.dto;

import javax.validation.constraints.NotBlank;

public class ClientCreateDto extends UserCreateDto{
	
	@NotBlank
    private String passportNumber;
    private Integer numberOfReservations;

    public String getPassportNumber() {
        return passportNumber;
    }

    public Integer getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setNumberOfReservations(Integer numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

}
