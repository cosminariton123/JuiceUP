package com.example.juiceup;

public class CurrentUser {

    private Boolean is_logged = false;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private String salt;
    private Double trust_score;
    private Integer car_max_km_range;
    private Integer guarded_place_preference;
    private Integer nr_of_parking_spots_preference;
    private Integer type2_preference;
    private Integer wall_preference;
    private Integer supercharger_preference;
    private Integer min_kwh_preference;
    private Integer min_rating_preference;
    private Integer min_trust_preference;

    private static CurrentUser instance = new CurrentUser();

    private CurrentUser(){
        internal_set_detailes("", "", "", "", "", 0, 0, 0, 0, 0, 0, 0, 1, 1,0. ,false);
    }

    private void internal_set_detailes(String mail, String pass, String input_salt, String f_name, String l_name, Integer max_km, Integer guarded_place_preferenc, Integer nr_of_parking_spots_preferenc,
            Integer type2_preferenc, Integer wall_preferenc, Integer supercharger_preferenc, Integer min_kwh_preferenc, Integer min_rating_preferenc, Integer min_trust_preferenc, Double trust, Boolean logged){
        is_logged = logged;
        email = mail;
        first_name = f_name;
        last_name = l_name;
        password = pass;
        salt = input_salt;
        trust_score = trust;
        car_max_km_range = max_km;
        guarded_place_preference = guarded_place_preferenc;
        nr_of_parking_spots_preference = nr_of_parking_spots_preferenc;
        type2_preference = type2_preferenc;
        wall_preference = wall_preferenc;
        supercharger_preference = supercharger_preferenc;
        min_kwh_preference = min_kwh_preferenc;
        min_trust_preference = min_trust_preferenc;
        min_rating_preference = min_rating_preferenc;
    }

    public static CurrentUser getInstance(){
        return instance;
    }

    public void set_details(String mail, String pass, String salt, String f_name, String l_name, Integer max_km, Integer guarded_place_preference,
            Integer nr_of_parking_spots_preference, Integer type2_preference, Integer wall_preference, Integer supercharger_preference, Integer min_kwh_preference, Integer min_rating_preference, Integer min_trust_preference, Double trust){
        internal_set_detailes(mail, pass, salt,f_name, l_name, max_km, guarded_place_preference, nr_of_parking_spots_preference, type2_preference, wall_preference, supercharger_preference, min_kwh_preference, min_rating_preference, min_trust_preference , trust, true);
    }

    public void logout(){
        internal_set_detailes("", "", "", "", "", 0, 0, 0, 0
                , 0, 0, 0, 1 ,1 ,0., false);
    }

    public Integer get_min_trust_preference(){
        return  min_trust_preference;
    }

    public Integer get_min_rating_preference(){
        return min_rating_preference;
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

    public Double get_trust_score(){
        return trust_score;
    }

    public Integer get_car_max_km_range(){
        return car_max_km_range;
    }

    public Integer get_guarded_preference(){
        return guarded_place_preference;
    }

    public Integer get_of_parking_spots_preference(){
        return nr_of_parking_spots_preference;
    }

    public Integer get_type2_preference(){
        return type2_preference;
    }

    public Integer get_wall_preference(){
        return wall_preference;
    }

    public Integer get_supercharger_preference(){
        return supercharger_preference;
    }

    public Integer get_min_kwh_preference(){
        return min_kwh_preference;
    }
}
