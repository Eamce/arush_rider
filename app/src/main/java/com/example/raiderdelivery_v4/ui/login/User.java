package com.example.raiderdelivery_v4.ui.login;

public class User{

    private String id;
    private String username;
    private String password;
    private String fname;
    private String lname;
    private String image;
    private String r_id_num;
    //private String r_bunit_code;

    public void set_id(String temp_id)
    {
        this.id = temp_id;
    }
    public String get_id()
    {
        return this.id;
    }

    public void set_username(String temp_username)
    {
        this.username = temp_username;
    }
    public String get_username()
    {
        return this.username;
    }

    public void set_password(String temp_password)
    {
        this.password = temp_password;
    }
    public String get_password()
    {
        return this.password;
    }

    public void set_fname(String temp_fname)
    {
        this.fname = temp_fname;
    }
    public String get_fname()
    {
        return this.fname;
    }

    public void set_lname(String temp_lname)
    {
        this.lname = temp_lname;
    }
    public String get_lname()
    {
        return this.lname;
    }

    public void set_image(String temp_image)
    {
        this.image = temp_image;
    }
    public String get_image()
    {
        return this.image;
    }

    public void set_r_id_num(String temp_r_id_num)
    {
        this.r_id_num = temp_r_id_num;
    }
    public String get_r_id_num()
    {
        return this.r_id_num;
    }

//    public void set_r_bunit_code(String temp_r_bunit_code)
//    {
//        this.r_bunit_code = temp_r_bunit_code;
//    }
//    public String get_r_bunit_code()
//    {
//        return this.r_bunit_code;
//    }
}
