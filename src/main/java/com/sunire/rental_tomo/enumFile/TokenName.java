package com.sunire.rental_tomo.enumFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenName {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken")
    ;

    private String name;
}
