package com.TwinStar.TwinStar.chat.service;

import com.TwinStar.TwinStar.chat.domain.ChatMessage;
import com.TwinStar.TwinStar.chat.domain.ChatParticipant;
import com.TwinStar.TwinStar.chat.domain.ChatRoom;
import com.TwinStar.TwinStar.chat.domain.ReadStatus;
import com.TwinStar.TwinStar.chat.dto.ChatMessageDto;
import com.TwinStar.TwinStar.chat.dto.ChatRoomCreateReqDto;
import com.TwinStar.TwinStar.chat.dto.ChatRoomResDto;
import com.TwinStar.TwinStar.chat.repository.ChatMessageRepository;
import com.TwinStar.TwinStar.chat.repository.ChatParticipantRepository;
import com.TwinStar.TwinStar.chat.repository.ChatRoomRepository;
import com.TwinStar.TwinStar.chat.repository.ReadStatusRepository;
import com.TwinStar.TwinStar.user.domain.User;
import com.TwinStar.TwinStar.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatMessageRepository chatMessageRepository, ReadStatusRepository readStatusRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
    }

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    User sender = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));

    //    채팅 방 개설
    public Long RoomOpen(ChatRoomCreateReqDto dto){

//        채팅방 생성
        String isGroupChat = dto.getIdList().size()>2 ? "Y" : "N";
        ChatRoom chatRoom = ChatRoom.builder()
                .name(dto.getChatRoomName())
                .isGroupChat(isGroupChat)
                .build();
        chatRoomRepository.save(chatRoom);

//        채팅방 참여자 추가
        for (Long chatParticipantId : dto.getIdList()){
            User user = userRepository.findById(chatParticipantId).orElseThrow(()-> new EntityNotFoundException("user is not found."));
            ChatParticipant chatParticipant = ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .build();
            chatParticipantRepository.save(chatParticipant);
        }

        return chatRoom.getId();
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResDto> chatRoomList(){

//        현재 유저 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("user is not found."));

//        현재 유저의 참여중인 방 확인 (방 업데이트 내림차순 -> 최신순이라고 보면됨)
        List<ChatRoomResDto> participationRoomList = chatRoomRepository.findChatRoomsWithUnreadCount(user).orElseThrow(()-> new EntityNotFoundException("참여 중인 채팅방이 없습니다."));

//        만약 1:1채팅방이면 상대방 닉네임으로 방제목 전달
//        그 뭐야 방별로 그룹채팅인지 확인하고 아니면 참여자 리스트 만들어서 나 제외하고 상대 유저 닉네임으로 방제목, 이미지
//        카톡마냥 그룹채팅방인데 나 혼자 있으면 대화 상대 없음으로하고 이미지 기본이미지
        participationRoomList.forEach(chatRoomResDto -> {
            List<ChatParticipant> participantList =  chatParticipantRepository.findAllByChatRoomId(chatRoomResDto.getRoomId());
            User you = participantList.stream()
                    .map(ChatParticipant::getUser)
                    .filter(u -> !u.getId().equals(user.getId()))
                    .findFirst()
                    .orElse(null);

            if (chatRoomResDto.getIsGroupChat().equals("N")) {
                if(participantList.size()==2) {
                    if (you != null) {
                        chatRoomResDto.setRoomName(you.getNickName());
                        chatRoomResDto.setRoomImage(you.getProfileImg());
                    }
                }
            }else {
                if(participantList.size()==1) {
                    chatRoomResDto.setRoomName("대화 상대 없음");
                    chatRoomResDto.setRoomImage("https://i.pinimg.com/474x/3b/73/a1/3b73a13983f88f8b84e130bb3fb29e17.jpg");
                }else{
                    chatRoomResDto.setRoomImage(you.getProfileImg());
                }
            }
        });
        return participationRoomList;
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatMessages(Long roomId, Pageable pageable){
//        채팅방에 최신순으로 메세지 불러오기 / 20개씩은 컨트롤러에서 하는중
        Page<ChatMessage> chatMessagePage = chatMessageRepository.findByChatRoomIdOrderByCreatedTimeDesc(roomId, pageable);

//        메세지를 dto로 조립ㅂ하기
        Page<ChatMessageDto> ChatMessageDtoList = chatMessagePage.map(chatMessage -> ChatMessageDto.builder()
                        .messageId(chatMessage.getId())
                        .senderNickName(chatMessage.getUser().getNickName())
                        .message(chatMessage.getContent())
                        .sendTime(chatMessage.getCreatedTime())
                        .notReadCount(chatMessageRepository.countUnreadUsers(roomId, chatMessage.getId()))
                        .build()
                );
        return ChatMessageDtoList;
    }

    // 메ㅅㅔ지 저장
    public void messageSave(ChatMessageDto chatMessageDto, Long roomId) {
        User sender = userRepository.findByNickName(chatMessageDto.getSenderNickName()).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다."));
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다.")))
                .user(sender)
                .content(chatMessageDto.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);

        //    메세지 전송할 때 읽음 여부를 참여자들 false로 하기
        List<ChatParticipant> participants = chatParticipantRepository.findAllByChatRoomId(chatMessage.getChatRoom().getId());

        for (ChatParticipant participant : participants) {
            User user = participant.getUser();
            Boolean isRead = user.equals(sender);
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatMessage.getChatRoom())
                    .chatMessage(chatMessage)
                    .user(participant.getUser())
                    .isRead(isRead) // 초기값은 읽지 않지만~ 보낸 사람은 자기 거 읽음 처리
                    .build();
            readStatusRepository.save(readStatus);
        }
    }

    public void readChat(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(()-> new EntityNotFoundException("유저가 없어요."));
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new EntityNotFoundException("채팅방이 없습니다."));

        List<ReadStatus> unreadMessageList = readStatusRepository.findAllByUserAndChatMessage_ChatRoom(user, chatRoom);
        System.out.println(unreadMessageList);
        for (ReadStatus readStatus : unreadMessageList) {
            readStatus.updateIsRead(true);
        }
    }

    public void leaveChatRoom(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."));

        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserId(roomId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방 참여자가 아닙니다."));

        chatParticipant.leaveChatRoom();
    }
}
