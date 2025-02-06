package com.TwinStar.TwinStar.user.service;

;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.dto.LoginDto;
import com.TwinStar.TwinStar.user.dto.UserListDto;
import com.TwinStar.TwinStar.user.dto.UserSaveReq;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Long create(UserSaveReq dto) throws IllegalArgumentException {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복 이메일입니다.");
        }
        User member = userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return member.getId();
    }

    public List<UserListDto> findAll(){
        return userRepository.findAll().stream().map(user -> user.listFromEntity()).collect(Collectors.toList());
    }

}
