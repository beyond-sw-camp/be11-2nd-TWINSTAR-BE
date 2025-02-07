package com.TwinStar.TwinStar.user.service;

import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.follow.repository.FollowRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.*;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
    }

    public User login(LoginDto dto){
        boolean check = true;
//        email존재여부
        Optional<User> optionalMember = userRepository.findByEmail(dto.getEmail());
        if(!optionalMember.isPresent()){
            check = false;
        }
//        password일치 여부
        if(!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())){
            check =false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalMember.get();
    }

//    public void checkUserRestrictions(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
//
//        if (user.getUserStatus() == UserStatus.RESTRICTED) {
//            throw new RuntimeException("제재된 유저는 게시물 및 댓글 작성이 불가합니다.");
//        }

//        if (user.getUserStatus() == UserStatus.BAN) {
//            throw new RuntimeException("정지된 유저는 접근할 수 없습니다.");
//        }
//    }

//    회원가입
    public Long create(UserSaveReq dto) throws IllegalArgumentException {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복 이메일입니다.");
        }
        User member = userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return member.getId();
    }

//  상대 프로필조회
    public UserProfileDto searchProfile(Long id) throws NoSuchElementException, RuntimeException{
        Long followingCount = followRepository.countByFollowing(id);
        Long followerCount = followRepository.countByFollower(id);//countByFollowing의 매개변수를 Long타입으로 바꿔야함
        User user = userRepository.findByIdWithPosts(id)//프로필dto 매개변수를 위해 사용
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("등록되지 않은 사용자입니다."))
                .detailFromEntity(followerCount,followingCount,user.getPosts());//프로필 데이터
    }

    //  내 프로필조회
    public UserProfileDto searchProfile() throws NoSuchElementException, RuntimeException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.valueOf((authentication.getName()));
        User user = userRepository.findByIdWithPosts(id)//프로필dto 매개변수를 위해 사용
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long followingCount = followRepository.countByFollowing(id);
        Long followerCount = followRepository.countByFollower(id);
        return userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("등록되지 않은 사용자입니다."))
                .detailFromEntity(followerCount,followingCount,user.getPosts());//프로필 데이터
    }

        //    관리자용 유저 리스트
    public List<UserListDto> userList(){
        return userRepository.findAll().stream().map(m->m.listFromEntity()).toList();
    }
}
