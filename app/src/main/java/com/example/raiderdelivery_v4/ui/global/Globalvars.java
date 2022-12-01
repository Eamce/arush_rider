package com.example.raiderdelivery_v4.ui.global;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

public class Globalvars
{
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static Activity activity;
    public static Context context;

    public static SQLiteDatabase mydatabase;

    public static String secretKey = "SoAxVBnw8PYHzHHTFBQdG0MFCLNdmGFf";
    public static String EmptyEncrypted = "VDFnOTk0eG8yVUFxRzgxTcHRUrp/3YpLH7mmTEh/nSg=\n";

//    Online(Food)
//    public static String online_link = "https://app2.alturush.com/";
//    public static String online_product_image_link = "https://storetenant.alturush.com/storage/";
//    public static String online_discount_image_link = "https://storetenant.alturush.com/";
//    public static String online_profile_image_link = "https://customerservice.alturush.com/";
//    public static String online_tenant_image_link = "https://storetenant.alturush.com/storage/";
////    Online(Grocery)
//    public static String gc_online_link = "https://app2.alturush.com/";
//    public static String gc_online_product_image_link = "https://storetenant.alturush.com/storage/";
//    public static String gc_online_discount_image_link = "https://storetenant.alturush.com/";
//    public static String gc_online_profile_image_link = "https://customerservice.alturush.com/";
//    public static String gc_online_tenant_image_link = "https://storetenant.alturush.com/storage/";

    //offline(Food)
    public static String online_link = "http://172.16.43.125/e-commerce_rider/index.php/";
    public static String online_product_image_link = "http://172.16.43.195:85/storage/";
    public static String online_discount_image_link = "http://172.16.43.125/e-commerce_rider/";
    public static String online_discount_image_link2 = "http://172.16.43.239:2000/storage/uploads/discount_ids/";
    public static String online_profile_image_link = "http://172.16.43.195:83/";
    public static String online_tenant_image_link = "http://172.16.43.195:84/";
    //    offline(Grocery)
    public static String gc_online_product_image_link = "https://admins.alturush.com/ITEM-IMAGES/";
    public static String gc_online_link = "http://172.16.43.125/e-commerce_rider/index.php/";
    public static String gc_online_discount_image_link = "http://172.16.43.125/e-commerce_rider/";
    public static String gc_online_discount_image_link2 = "http://172.16.43.239:2000/storage/uploads/discount_ids/";
    public static String gc_online_profile_image_link = "http://172.16.43.195:83/";
    public static String gc_online_tenant_image_link = "http://172.16.43.195:84/";

    //test live(Food)
//    public static String online_link = "http://203.177.223.59/app2.alturush/index.php/";
//    public static String online_product_image_link = "https://storetenant.alturush.com/storage/";
//    public static String online_discount_image_link = "https://storetenant.alturush.com/";
//    public static String online_profile_image_link = "https://customerservice.alturush.com/";
//    public static String online_tenant_image_link = "https://storetenant.alturush.com/storage/";
    //test live(Grocery)
//    public static String gc_online_product_image_link = "https://admins.alturush.com/ITEM-IMAGES/";
//    public static String gc_online_product_image_link2 = "https://storetenant.alturush.com/storage/";
//    public static String gc_online_discount_image_link = "https://storetenant.alturush.com/";
//    public static String gc_online_profile_image_link = "https://customerservice.alturush.com/";
//    public static String gc_online_tenant_image_link = "https://storetenant.alturush.com/storage/";

    public Globalvars(Context context, Activity act){
        preferences = act.getSharedPreferences("Global_vars",context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void logout()
    {
        editor.clear();
        editor.commit();
    }
    public void set(String varname, String varval)
    {
        editor.putString(varname,varval);
        editor.commit();
    }
    public String get(String varname)
    {
        return preferences.getString(varname,null);
    }


}
