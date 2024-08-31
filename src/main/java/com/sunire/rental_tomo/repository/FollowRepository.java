package com.sunire.rental_tomo.repository;

import com.sunire.rental_tomo.domain.entity.Follow;
import com.sunire.rental_tomo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User user);
    List<Follow> findByFollowed(User user);
    void deleteByFollowerAndFollowed(User follower, User followed);
    int countByFollowed(User follower);
    int countByFollower(User followed);
    int countByFollowedAndFollower(User followed, User follower);
}
