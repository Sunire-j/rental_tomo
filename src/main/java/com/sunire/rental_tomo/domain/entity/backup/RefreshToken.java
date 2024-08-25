//package com.sunire.rental_tomo.domain.entity.backup;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import java.time.Instant;
//import java.util.Date;
//
//@Builder
//@Getter
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "refresh_token", schema = "rental_tomo")
//public class RefreshToken {
//    @Id
//    @Column(name = "user_id", nullable = false, length = 45)
//    private String userId;
//
//    @Column(name = "token", nullable = false)
//    private String token;
//
//    @Column(name = "expiration_date", nullable = false)
//    private Date expirationDate;
//
//    @ColumnDefault("CURRENT_TIMESTAMP")
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "userid")
//    private User user;
//}