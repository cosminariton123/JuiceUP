package com.example.juiceup;

public class CurrentUser {

    private Boolean is_logged = false;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private String salt;
    private Integer trust_score;

    private static CurrentUser instance = new CurrentUser();

    private CurrentUser(){
        internal_set_detailes("", "", "", "", "", 0 ,false);
    }

    private void internal_set_detailes(String mail, String pass, String input_salt, String f_name, String l_name, Integer trust, Boolean logged){
        is_logged = logged;
        email = mail;
        first_name = f_name;
        last_name = l_name;
        password = pass;
        salt = input_salt;
        trust_score = trust;
    }

    public static CurrentUser getInstance(){
        return instance;
    }

    public void set_details(String mail, String pass, String salt, String f_name, String l_name, Integer trust){
        internal_set_detailes(mail, pass, salt,f_name, l_name, trust, true);
    }

    public void logout(){
        internal_set_detailes("", "", "", "", "", 0, false);
    }

    public Boolean get_is_logged(){
        return is_logged;
    }

    public String get_email(){
        return email;
    }

    public String get_first_name(){
        return first_name;
    }

    public String get_last_name(){
        return last_name;
    }

    public String get_salt(){
        return salt;
    }

    public String get_password(){
        return password;
    }

    public Integer get_trust_score(){
        return trust_score;
    }
}
