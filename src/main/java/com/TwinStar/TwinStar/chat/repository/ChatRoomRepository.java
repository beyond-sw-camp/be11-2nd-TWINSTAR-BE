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

    @Query("SELECT new com.TwinStar.TwinStar.chat.dto.ChatRoomResDto( " +
            "c.id, c.name, COUNT(r.id), c.isGroupChat) " +
            "FROM ChatRoom c " +
            "JOIN ChatParticipant cp ON cp.chatRoom = c " +
            "LEFT JOIN ReadStatus r ON r.chatRoom = c AND r.user = :user AND r.isRead = false " +
            "WHERE cp.user = :user " +
            "GROUP BY c.id, c.name, c.isGroupChat, c.updatedTime " +
            "ORDER BY c.updatedTime DESC")
    Optional<List<ChatRoomResDto>> findChatRoomsWithUnreadCount(@Param("user") User user);
}
