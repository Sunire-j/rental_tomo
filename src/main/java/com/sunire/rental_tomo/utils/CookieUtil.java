package com.sunire.rental_tomo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

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

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }


}
