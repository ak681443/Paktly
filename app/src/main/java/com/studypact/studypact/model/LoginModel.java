package com.studypact.studypact.model;

/**
 * Created by Kartikk on 11/13/2016.
 */

import org.simpleframework.xml.Attribute;


public class LoginModel {

    public LoginModel(String first_name, String last_name, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
    }

    @Attribute
    public String first_name;

    @Attribute
    public String last_name;

    @Attribute
    public String phone;


}
