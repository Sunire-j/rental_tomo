package com.sunire.rental_tomo.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "user", schema = "rental_tomo")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid", nullable = false, length = 45)
    private String userid;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "phonenum", nullable = false)
    private String phonenum;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING) // ENUM 타입으로 지정
    private Sex sex;

    @Column(name = "birth", nullable = false)
    private LocalDate birth; // LocalDate로 변경

    @Column(name = "SNS", length = 45)
    private String sns;

    @Column(name = "profilesrc", length = 255)
    private String profilesrc;

    @Column(name = "email", length = 100)
    private String email;

    public enum Sex {
        M, F
    }
}