package com.raf.user_sevice.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.raf.user_sevice.domain.User;

@Entity
//dodajemo indexe i oznacavamo da username i email moraju biti unique
//tj na username i email kolone smo stavili unique indexe			
//inace se indexi koriste da ubrzaju pretragu-kada pretrazujemo po index polju slozenost je logaritamska 
@Table(indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "email", unique = true)})		
public class Client extends User{

	private String passportNumber;
    private Integer numberOfReservations;
    private String rank;

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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
