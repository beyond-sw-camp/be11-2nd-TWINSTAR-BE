package com.TwinStar.TwinStar.user.service;

;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.LoginDto;
import com.TwinStar.TwinStar.user.dto.UserProfileDto;
import com.TwinStar.TwinStar.user.dto.UserSaveReq;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public Long create(UserSaveReq dto) throws IllegalArgumentException {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복 이메일입니다.");
        }
        User member = userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return member.getId();
    }

    public UserProfileDto findById(Long id) throws NoSuchElementException, RuntimeException{
        return userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("없는 id입니다."))
                .detailFromEntity();
    }
}
