package com.sunire.rental_tomo.utils;

import org.springframework.ui.Model;

public class setLoginStatus {

    public static Model setLogin(Model model, String name){
        model.addAttribute("isLogin", "Y");
        model.addAttribute("username", name);
        return model;
    }
}
