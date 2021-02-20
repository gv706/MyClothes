package com.example.clothes;

import java.io.Serializable;

public class Personal  {
    String name,gender,email,id,phonenumber;
    public Personal() {
    }

    public Personal(String id,String name,  String gender, String email,String phone) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.id=id;
        this.phonenumber=phone;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
