package com.TwinStar.TwinStar.chat.repository;

import com.TwinStar.TwinStar.chat.domain.ChatMessage;
import com.TwinStar.TwinStar.chat.domain.ChatRoom;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    나간적이 있다면 나간 이후의 채팅방의 메세지를 페이징 처리해서 조회하는거 내림차순 (초ㅣ신순)
    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE cm.chatRoom = :chatRoom " +
            "AND cm.createdTime > " +
            "(SELECT cp.updatedTime FROM ChatParticipant cp " +
            "WHERE cp.chatRoom = :chatRoom AND cp.user = :user) " +
            "ORDER BY cm.createdTime DESC")
    Page<ChatMessage> findMessagesAfterLastUpdate(@Param("chatRoom") ChatRoom chatRoom, @Param("user") User user, Pageable pageable);

//    메세지 읽지 않은 유저 수 세기
    @Query("SELECT COALESCE(COUNT(cp), 0) FROM ChatParticipant cp " +
            "WHERE cp.chatRoom.id = :chatRoomId " +
            "AND cp.user.id NOT IN " +
            "(SELECT rs.user.id FROM ReadStatus rs WHERE rs.chatMessage.id = :chatMessageId)")
    Long countUnreadUsers(@Param("chatRoomId") Long chatRoomId, @Param("chatMessageId") Long chatMessageId);
}
