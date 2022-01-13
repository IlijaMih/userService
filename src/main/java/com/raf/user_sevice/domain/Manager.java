package com.raf.user_sevice.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
//dodajemo indexe i oznacavamo da username i email moraju biti unique
//tj na username i email kolone smo stavili unique indexe			
//inace se indexi koriste da ubrzaju pretragu-kada pretrazujemo po index polju slozenost je logaritamska 
@Table(indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "email", unique = true)})	
public class Manager extends User{

	private String hotelName;
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
