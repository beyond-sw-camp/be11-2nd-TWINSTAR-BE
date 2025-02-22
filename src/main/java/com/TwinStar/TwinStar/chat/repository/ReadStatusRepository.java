package com.TwinStar.TwinStar.chat.repository;

import com.TwinStar.TwinStar.chat.domain.ChatRoom;
import com.TwinStar.TwinStar.chat.domain.ReadStatus;
import com.TwinStar.TwinStar.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

    List<ReadStatus> findAllByUserAndChatMessage_ChatRoom(User user, ChatRoom chatRoom);
}
