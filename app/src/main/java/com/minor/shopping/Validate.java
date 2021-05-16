package com.minor.shopping;

import android.text.TextUtils;
import android.util.Patterns;

public class Validate {
    private static final Validate instance = new Validate(); //single instance of the class
    private Validate(){
        //private empty constructor to prevent object initiation
    }

    public static Validate getInstance(){
        return instance;
    }

    boolean email(String target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    boolean password(String pass){
        return (pass.length()>=6);
    }

    boolean string(String str){
        return (!TextUtils.isEmpty(str));
    }
    boolean pincode(String pin){
        return pin.length()==6;
    }
}
