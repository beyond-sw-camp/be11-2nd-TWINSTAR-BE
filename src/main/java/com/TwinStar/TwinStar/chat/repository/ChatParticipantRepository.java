package com.TwinStar.TwinStar.chat.repository;

import com.TwinStar.TwinStar.chat.domain.ChatParticipant;
import com.TwinStar.TwinStar.chat.domain.ChatRoom;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

//    roomId로 참여 user 찾기 리스트로
    List<ChatParticipant> findAllByChatRoomId(Long chatRoomId);

//    우ㅠ저가 그 채팅방에 있느지 확인
    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.chatRoom.id = :chatRoomId AND cp.user.id = :userId")
    Optional<ChatParticipant> findByChatRoomIdAndUserId(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
}
