package com.sunire.rental_tomo.service;


import com.sunire.rental_tomo.domain.entity.Follow;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.exception.ErrorCode;
import com.sunire.rental_tomo.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public Boolean addFollow(User follower, User followed) {

        try{
            Follow f = Follow.builder().
                    followed(followed).
                    follower(follower).
                    build();

            followRepository.save(f);
        }catch(Exception e){
            throw new AppException(ErrorCode.DB_ERROR, "알 수 없는 오류 발생");
        }
        return true;
    }

    @Transactional
    public Boolean unfollow(User follower, User followed) {
        try{
            followRepository.deleteByFollowerAndFollowed(follower, followed);
        }catch(Exception e){
            System.out.println("오류"+ e);
            throw new AppException(ErrorCode.DB_ERROR, "알 수 없는 오류 발생");
        }
        return true;
    }

    public boolean isFollow(User follower, User followed) {
        int count = followRepository.countByFollowedAndFollower(followed, follower);
        return count != 0;
    }

    public Map<String, Integer> getAllFollowCount(User user){
        Map<String, Integer> map = new HashMap<>();
        map.put("followed", followRepository.countByFollower(user));
        map.put("follower", followRepository.countByFollowed(user));
        return map;
    }

    public List<Follow> getFollowerList(User user){
        return followRepository.findByFollowed(user);
    }

    public List<Follow> getFollowedList(User user){
        return followRepository.findByFollower(user);
    }

}
