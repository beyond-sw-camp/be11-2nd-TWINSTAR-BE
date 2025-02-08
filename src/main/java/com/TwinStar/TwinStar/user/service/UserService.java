package com.TwinStar.TwinStar.user.service;

import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.common.exception.PrivateAccountException;
import com.TwinStar.TwinStar.common.exception.SuspendedAccountException;
import com.TwinStar.TwinStar.follow.domain.Follow;
import com.TwinStar.TwinStar.follow.repository.FollowRepository;
import com.TwinStar.TwinStar.user.domain.IdVisibility;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import com.TwinStar.TwinStar.user.dto.*;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
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
        userRepository.flush();
//        email존재여부
        Optional<User> optionalMember = userRepository.findByEmail(dto.getEmail());
        System.out.println(userRepository.findByEmail(dto.getEmail()));
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
        System.out.println(userRepository.findByEmail(dto.getEmail()));
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복 이메일입니다.");
        }
//        닉네임 중복체크 메서드
        if (userRepository.existsByNickName(dto.getNickName())){
            throw new IllegalArgumentException("중복된 닉네임입니다");
        }
        User user = userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return user.getId();
    }

//  상대 프로필조회
    public UserProfileDto searchProfile(Long id) throws NoSuchElementException, RuntimeException{
        Long followingCount = followRepository.countByFollowing(id);
        Long followerCount = followRepository.countByFollower(id);//countByFollowing의 매개변수를 Long타입으로 바꿔야함

        User receiveUser = userRepository.findByIdWithPosts(id)//프로필dto 매개변수를 위해 사용
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long myId = Long.valueOf(authentication.getName());

//        분기처리후 프로필 보여야 하는것도 생각해야함

//            정지된 계정일 경우 에러 처리
        if (receiveUser.getUserStatus() == UserStatus.BAN){
            throw new SuspendedAccountException("해당 계정은 정지되었습니다.");
        }

//        비공개 계정일 경우, 현재 로그인한 사용자가 친구가 아닐 경우
        boolean isFollow = followRepository.existsByFollowerAndFollowing(myId,receiveUser.getId());//팔로우 레포에서 매개변수 변경해야함
        if (receiveUser.getIdVisibility() == IdVisibility.FOLLOW && !isFollow){
            throw new PrivateAccountException("이 계정은 비공개 상태입니다.");
        }
        if(receiveUser.getIdVisibility() == IdVisibility.ONLYME){
            throw new PrivateAccountException("이 계정은 비공개 상태입니다.");
        }

        return userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("등록되지 않은 사용자입니다."))
                .detailFromEntity(followerCount,followingCount,receiveUser.getPosts());//프로필 데이터
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

    @Transactional
    public void updateUserProfile(Long id, UserProfileUpdateDto updateDto) {
        // 1. 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 닉네임 중복 체크 (옵션)
        if (!user.getNickName().equals(updateDto.getNickName()) &&
                userRepository.existsByNickName(updateDto.getNickName())) {
            throw new RuntimeException("This nickname is already taken.");
        }

        // 3. 사용자 정보 변경
        user.updateProfile(updateDto.getNickName(), updateDto.getProfileTxt()
                , updateDto.getSex(), updateDto.getIdVisibility());
    }

    @Transactional
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        // userId를 이용하여 유저 조회
        User user = userRepository.findByIdAndDelYn(userId, YN.N)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 상태 변경 메서드 호출
        user.deleteUser();

        userRepository.save(user); // 변경사항 저장
    }





        //    관리자용 유저 리스트
    public List<UserListDto> userList(){
        return userRepository.findAll().stream().map(m->m.listFromEntity()).toList();
    }
}
