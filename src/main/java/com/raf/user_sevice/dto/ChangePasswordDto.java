package com.raf.user_sevice.dto;

public class ChangePasswordDto {

    private String password;

    public ChangePasswordDto() {

    }

    public ChangePasswordDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
