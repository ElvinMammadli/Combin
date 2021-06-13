package com.quizz.model;

public class User {
    private String name;
    private String user_name;
    private String email;
    private String gender;
    private String rank;
    private String country;
    private String url;
    public User(){

    }
    public User(String name, String user_name , String gender, String country,String email,String url) {
        this.name = name;
        this.user_name=user_name;
        this.country=country;
        this.gender = gender;
        this.email=email;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
