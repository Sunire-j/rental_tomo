package com.sunire.rental_tomo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    //DO NOT TOUCH THIS CODE

    public static String getCookie(String name, HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    token = c.getValue();
                    break;
                }
            }
        }else{
            return null;
        }
        return token;
    }

    public static String getCookie_Bearer(String name, HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    token = c.getValue();
                    break;
                }
            }
        }else{
            return null;
        }
        return "Bearer " + token;
    }

}
