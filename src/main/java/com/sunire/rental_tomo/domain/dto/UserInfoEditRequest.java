package com.sunire.rental_tomo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEditRequest {

    private Long id;
    private String nickname;
    private String email;
    private String sns;
}
