package com.TwinStar.TwinStar.chat.repository;

import com.TwinStar.TwinStar.chat.domain.ChatRoom;
import com.TwinStar.TwinStar.chat.dto.ChatRoomResDto;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

//    채팅방에 참여되어 있는 것을 updated 타임 기준으로 정렬
    @Query("SELECT new com.TwinStar.TwinStar.chat.dto.ChatRoomResDto( " +
            "c.id, c.name, COUNT(r.id), c.isGroupChat, cp.isActive, " +
            "CASE WHEN c.isGroupChat = 'Y' THEN (SELECT COUNT(cp2) FROM ChatParticipant cp2 WHERE cp2.chatRoom = c) ELSE 0 END, " +
            "c.updatedTime) " +
            "FROM ChatRoom c " +
            "JOIN ChatParticipant cp ON cp.chatRoom = c " +
            "LEFT JOIN ReadStatus r ON r.chatRoom = c AND r.user = :user AND r.isRead = false " +
            "WHERE cp.user = :user AND cp.isActive = true " +
            "GROUP BY c.id, c.name, c.isGroupChat, cp.isActive, c.updatedTime " +
            "ORDER BY c.updatedTime DESC")
    Optional<List<ChatRoomResDto>> findActiveChatRooms(@Param("user") User user);


    //    1:1방인지 확인
    @Query("SELECT c FROM ChatRoom c " +
            "JOIN ChatParticipant cp1 ON cp1.chatRoom = c AND cp1.user.id = :userId1 " +
            "JOIN ChatParticipant cp2 ON cp2.chatRoom = c AND cp2.user.id = :userId2 " +
            "WHERE c.isGroupChat = 'N'")
    Optional<ChatRoom> findPrivateChatRoom(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
