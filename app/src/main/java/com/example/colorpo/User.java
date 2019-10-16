package com.example.colorpo;

public class User {
    private String fname;
    private String lname;
    private Long mobile;
    private String email;

    public User(){}

    public User(String fname, String lname, Long mobile, String email) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
