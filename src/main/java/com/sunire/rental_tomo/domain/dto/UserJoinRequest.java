package com.sunire.rental_tomo.domain.dto;

import com.sunire.rental_tomo.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private Long id;
    private String userid;
    private String password;
    private String nickname;
    private String phonenum;
    private User.Sex sex; // 여전히 String으로 유지할 수 있지만, 필요시 LocalDate로 변경 가능
    private LocalDate birth; // LocalDate로 변경
    private String email;
}