package com.TwinStar.TwinStar.user.service;


import com.TwinStar.TwinStar.common.domain.Visibility;
import com.TwinStar.TwinStar.common.domain.YN;
import com.TwinStar.TwinStar.common.exception.PrivateAccountException;
import com.TwinStar.TwinStar.common.exception.SuspendedAccountException;
import com.TwinStar.TwinStar.common.service.S3Service;
import com.TwinStar.TwinStar.follow.repository.FollowRepository;
import com.TwinStar.TwinStar.user.domain.AdminYn;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.domain.UserStatus;
import com.TwinStar.TwinStar.user.dto.*;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final S3Service s3Service;
    private final String DEFAULT_PROFILE_IMAGE = "https://example.com/default_profile.jpg"; //기본 이미지 url

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowRepository followRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
        this.s3Service = s3Service;
    }

//   1. 로그인
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

//   2. 회원가입
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

//  3. 상대 프로필조회
    public UserProfileDto searchProfile(Long receiveUserId) throws NoSuchElementException, RuntimeException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("user not found"));
        User receiveUser = userRepository.findById(receiveUserId).orElseThrow(()->new EntityNotFoundException("user not found"));
        Long followingCount = followRepository.countByReceiveUserIdAndFollowYn(receiveUser,YN.Y);
        Long followerCount = followRepository.countByUserIdAndFollowYn(user,YN.Y);//countByFollowing의 매개변수를 Long타입으로 바꿔야함

//        User receiveUserPost = userRepository.findByIdWithPosts(receiveUserId)//프로필dto 매개변수를 위해 사용
//                .orElseThrow(() -> new RuntimeException("User not found"));

//            정지된 계정일 경우 에러 처리
        if (receiveUser.getUserStatus() == UserStatus.BAN){
            throw new SuspendedAccountException("해당 계정은 정지되었습니다.");
        }

//        비공개 계정일 경우, 현재 로그인한 사용자가 친구가 아닐 경우
        boolean isFollow = followRepository.existsByUserIdAndReceiveUserId(user,receiveUser);//팔로우 레포에서 매개변수 변경해야함
        if (receiveUser.getIdVisibility() == Visibility.FOLLOW && !isFollow){
            throw new PrivateAccountException("이 계정은 비공개 상태입니다.");
        }
        if(receiveUser.getIdVisibility() == Visibility.ONLYME){
            throw new PrivateAccountException("이 계정은 비공개 상태입니다.");
        }

//        자신을 조회할 수 없음
        if (user.getId().equals(receiveUser.getId())) {
            throw new IllegalArgumentException("자신의 프로필을 조회할 수 없습니다.");
        }

//        탈퇴한 계정
        if (receiveUser.getDelYn() == YN.Y) {
            throw new EntityNotFoundException("해당 계정은 탈퇴한 사용자입니다.");
        }

        return UserProfileDto.profileSearch(receiveUser,followerCount,followingCount); //프로필dto로 전환해서 리턴
    }

//    4.  내 프로필조회
    public UserProfileDto searchProfile() throws NoSuchElementException, RuntimeException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        인증 검증
        if(authentication == null || authentication.getName() == null){
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다");
        }

        Long id = Long.valueOf((authentication.getName()));
        User user = userRepository.findByIdWithPosts(id)//프로필dto 매개변수를 위해 사용
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Long followingCount = followRepository.countByUserIdAndFollowYn(user,YN.Y);
        Long followerCount = followRepository.countByReceiveUserIdAndFollowYn(user,YN.Y);

        return UserProfileDto.profileSearch(user,followerCount,followingCount); //프로필dto로 전환해서 리턴
    }

//    5. 프로필 텍스트 업데이트
    public UserProfileDto updateUserProfile(UserProfileDto dto, MultipartFile profileImg) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다");
        }
        Long userId = Long.valueOf(authentication.getName());
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 닉네임 중복 체크 (옵션)
        if (!user.getNickName().equals(dto.getNickName()) &&
                userRepository.existsByNickName(dto.getNickName())) {
            throw new RuntimeException("This nickname is already taken.");
        }

        //  새 프로필 이미지 업로드 후 url저장
        String existingFileName = user.getProfileImg() != null ? extractFileName(user.getProfileImg()) : null;
        String newProfileImgUrl = s3Service.uploadFile(profileImg, profileImg.getOriginalFilename());
        user.updateProfileImage(newProfileImgUrl);
        userRepository.save(user);

        Long followingCount = followRepository.countByUserIdAndFollowYn(user,YN.Y);
        Long followerCount = followRepository.countByReceiveUserIdAndFollowYn(user,YN.Y);

        return UserProfileDto.fromEntity(user,followerCount,followingCount);
    }

//    프로필 이미지 업로드
    public String updateProfileImage(MultipartFile file) throws IOException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다");
        }
        Long userId = Long.valueOf(authentication.getName());
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        새 이미지 업록드
        String imageUrl = s3Service.uploadFile(file, file.getOriginalFilename());
        user.updateProfileImage(imageUrl);
        userRepository.save(user);
        return imageUrl;
    }

//    프로필 텍스트 수정
    public void updateProfileText(ProfileTextUpdateDto dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다");
        }
        Long userId = Long.valueOf(authentication.getName());
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User.builder()
                .nickName(dto.getNickName())
                .profileTxt(dto.getProfileTxt())
                .sex(dto.getSex())
                .idVisibility(dto.getIdVisibility())
                .build();

        userRepository.save(user);
    }


//   6. 회원탈퇴
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다.");
        }
        Long userId = Long.parseLong(authentication.getName());
        // userId를 이용하여 유저 조회
        User user = userRepository.findByIdAndDelYn(userId, YN.N)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 상태 변경 메서드 호출
        user.deleteUser();

        userRepository.save(user); // 변경사항 저장
    }

//   7. 비밀번호 변경
    public void changePassword(Long id, PasswordChangeRequest request, Authentication authentication){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 본인 인증 확인
        if (!user.getId().toString().equals(authentication.getName())){
            throw new SecurityException("비밀번호 변경 권한이 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 정책 검증 (예: 8자 이상, 숫자/특수문자 포함)
        if (!isValidPassword(request.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호가 보안 정책을 충족하지 않습니다.");
        }



        // 비밀번호 변경
        user.changePassword(request.getNewPassword(),passwordEncoder);
    }

//   8. 비밀번호 검증
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()].*");
    }


//    9. 계정범위 변경
    public void changeIdVisibility(Visibility newStatus){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 존재하지 않습니다.");
        }

        // newStatus가 null이면 예외 발생
        if (newStatus == null) {
            throw new IllegalArgumentException("변경할 계정 범위 값이 없습니다.");
        }


        Long userId = Long.valueOf(authentication.getName());
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User is not found"));

        // 기존 상태와 변경하려는 상태가 같으면 업데이트 불필요
        if (user.getIdVisibility() == newStatus) {
            throw new IllegalStateException("현재 계정 범위와 동일한 상태로 변경할 수 없습니다.");
        }
    //   상태 변경 메서드 호출
        user.changeStatus(newStatus);
    //   상태 변경 저장
        userRepository.save(user);
    }

//    채팅용 유저리스트
    public Page<ChatUserListDto> chatUserList(Pageable pageable, UserSearchDto dto) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(dto.getNickName())) {
                predicates.add(criteriaBuilder.equal(root.get("nickname"), dto.getNickName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable)
                .map(user -> new ChatUserListDto(user)); // User → ChatUserListDto 변환
    }


//  10. 관리자용 유저 리스트
    public Page<UserListDto> userList(Pageable pageable, UserSearchDto dto){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root : 엔티티의 속성을 접근하기 위한 객체, criteriabuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();
                if (dto.getNickName() != null){
                    predicates.add(criteriaBuilder.equal(root.get("nickname"),dto.getNickName()));
                }
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i =0; i<predicates.size();i++){
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        return userRepository.findAll(spec,pageable).map(user-> user.listFromEntity());
    }

//    관리자 유저 상세조회
    public UserDetailDto userDetailList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("user is not found"));
        return UserDetailDto.detailList(user);
    }


//    11. 관리자 권한 부여 메소드
    public void grantAdminRole(Long userid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long myId = Long.valueOf(authentication.getName());
        User myUser =  userRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User receiveUser = userRepository.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        ADMIN이 아니면 권한이 없어서 부여할수없음
        if (myUser.getAdminYn()!=(AdminYn.ADMIN)){
            throw new AccessDeniedException("권한없음");
        }
        if (myId == userid){
            throw new AccessDeniedException("자신의 계정에 권한부여 및 회수를 할 수 없음");
        }
        // 삭제된 계정인지 확인
        if (receiveUser.getDelYn() == YN.Y) {
            throw new IllegalStateException("삭제된 계정의 권한을 변경할 수 없습니다.");
        }

        receiveUser.changeAdmin(AdminYn.ADMIN);
    }
//    12. 관리자 권한 회수 메소드
    public void revokeAdminRole(Long userid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long myId = Long.valueOf(authentication.getName());
        User myUser =  userRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User receiveUser = userRepository.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (myUser.getAdminYn()!=(AdminYn.ADMIN)){
            throw new AccessDeniedException("권한없음");
        }
        if (myId == userid){
            throw new AccessDeniedException("자신의 계정에 권한부여 및 회수를 할 수 없음");
        }
        // 삭제된 계정인지 확인
        if (receiveUser.getDelYn() == YN.Y) {
            throw new IllegalStateException("삭제된 계정의 권한을 변경할 수 없습니다.");
        }
        receiveUser.changeAdmin(AdminYn.USER);
    }

//  13. 계정 정지
    public void banUser(Integer days) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminId = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("없는 관리자입니다."));
        User user = userRepository.findById(adminId.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        user.ban(days); // User 엔티티 내 메서드 호출
        userRepository.save(user);
    }

//  14.계정 정지 해제
    public void unbanUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User adminId = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()->new EntityNotFoundException("없는 관리자입니다."));
        User user = userRepository.findById(adminId.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        user.unban();
        userRepository.save(user);
    }

//    파일 경로에서 파일명만 추출하는 메서드
    private String extractFileName(String fileUrl){
        return fileUrl.substring(fileUrl.lastIndexOf("/")+1);
    }

}
