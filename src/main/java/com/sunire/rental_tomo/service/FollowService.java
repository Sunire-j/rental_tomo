package com.sunire.rental_tomo.service;


import com.sunire.rental_tomo.domain.entity.Follow;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.exception.AppException;
import com.sunire.rental_tomo.exception.ErrorCode;
import com.sunire.rental_tomo.repository.FollowRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Boolean unfollow(User follower, User followed) {
        try{
            followRepository.deleteByFollowerAndFollowed(follower, followed);
        }catch(Exception e){
            throw new AppException(ErrorCode.DB_ERROR, "알 수 없는 오류 발생");
        }
        return true;
    }

    public int getFollowedCount(User follower) {
        return followRepository.countByFollowed(follower);
        //내(특정 사람)가 팔로우중인 사람 수를 세는거라서 매개변수는 follower
    }

    public int getFollowerCount(User followed){
        return followRepository.countByFollower(followed);
        //나(특정 사람)를 팔로우하는 사람 수를 세는거라 followed
    }

}
