package com.TwinStar.TwinStar.blackList.service;

import com.TwinStar.TwinStar.blackList.domain.BlackList;
import com.TwinStar.TwinStar.blackList.dto.BlackListSearchDto;
import com.TwinStar.TwinStar.blackList.repository.BlackListRepository;
import com.TwinStar.TwinStar.common.exception.CustomException;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlackListService {
    @Autowired
    private BlackListRepository blacklistRepository;

    @Autowired
    private UserRepository userRepository;

//    차단
    public BlackList blockUser(Long userId, Long blockedUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 없다"));
        User blockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new RuntimeException("차단할 사용자를 찾을 수 없다"));

        // 이미 차단된 경우 예외 발생
        boolean isAlreadyBlocked = blacklistRepository.findByUserIdAndBlockedUserId(userId, blockedUserId).isPresent();
        if (isAlreadyBlocked) {
            throw new CustomException("이미 차단된 사용자입니다.");
        }

        BlackList blacklist = BlackList.builder()
                .user(user)
                .blockedUser(blockedUser)
                .build();

        return blacklistRepository.save(blacklist);
    }

//    차단해제
    public void unblockUser(Long userId, Long blockedUserId) {
        Optional<BlackList> blacklist = blacklistRepository.findByUserIdAndBlockedUserId(userId, blockedUserId);
        if (blacklist.isEmpty()) {
            throw new CustomException("차단기록을 찾을 수 없습니다.");
        }

        if (blacklist.isPresent()){//옵셔널 안에 값이 존재하는지
            BlackList foundBlackList = blacklist.get();//옵셔널에서 객체를 가져옴
            blacklistRepository.delete(foundBlackList);//삭제
        }
    }

//    차단목록 조회
    public Page<BlackList> getBlockedUsers(Long userId, Pageable pageable, BlackListSearchDto dto) {
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root : 엔티티의 속성을 접근하기 위한 객체, criteriabuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();
                if (dto.getBlockedUserNickName() != null){
                    predicates.add(criteriaBuilder.equal(root.get("nickname"),dto.getBlockedUserNickName()));
                }
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i =0; i<predicates.size();i++){
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
        return blacklistRepository.findByUserId(userId,pageable);
    }
}
