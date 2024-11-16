package com.jungle.studybbitback.domain.roommember;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.room.CreateRoomRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.GetRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.dto.roommember.InviteRoomMemberResponseDto;
import com.jungle.studybbitback.domain.room.dto.roommember.LeaveRoomMemberRequestDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.RoomMember;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.domain.room.service.RoomMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoomMemberServiceTest {

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RoomMemberService roomMemberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inviteRoomMember_shouldReturnInviteRoomMemberResponseDto() {
        // Given
        Long roomId = 1L;
        String email = "test@example.com";
        InviteRoomMemberRequestDto requestDto = new InviteRoomMemberRequestDto(roomId, email);

        Member member = new Member(1L, email, "password", "nickname", null);
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto(
                "Test Room",           // name
                "test-room-url",       // roomUrl
                null,                  // password (nullable)
                "Room detail",         // detail
                1,                     // participants
                10,                    // maxParticipants
                null,                  // profileImageUrl (nullable)
                false                  // isPrivate (공개 방)
        );
        Room room = new Room(roomId, createRoomRequestDto, member.getId());
        RoomMember roomMember = new RoomMember(room, member);

        // Mocking
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomMemberRepository.existsByRoomAndMember(room, member)).thenReturn(false);
        when(roomMemberRepository.save(any(RoomMember.class))).thenReturn(roomMember);

        // When
        InviteRoomMemberResponseDto response = roomMemberService.inviteRoomMember(requestDto);

        // Then
        assertNotNull(response);
        assertEquals(member.getId(), response.getMemberId());
        assertEquals(room.getId(), response.getRoomId());
    }

    @Test
    void getRoomMember_shouldReturnGetRoomMemberResponseDto() {
        // Given
        Long roomId = 1L;
        Long memberId = 1L;
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto(
                "Test Room",
                "test-room-url",
                null,
                "Room detail",
                1,
                10,
                null,
                false
        );
        Room room = new Room(createRoomRequestDto, memberId);
        Member member1 = new Member(1L, "test@example.com", "password", "nickname", null);
        Member member2 = new Member(2L, "test2@example.com", "password", "nickname2", null);
        RoomMember roomMember1 = new RoomMember(room, member1);
        RoomMember roomMember2 = new RoomMember(room, member2);
        List<RoomMember> roomMembers = List.of(roomMember1, roomMember2);

        // Mocking
        when(roomMemberRepository.findByRoomId(roomId)).thenReturn(roomMembers);

        // When
        List<GetRoomMemberResponseDto> response = roomMemberService.getRoomMembers(roomId);

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());  // 확인할 멤버 수가 2명이라고 가정
        assertEquals(member1.getId(), response.get(0).getRoomId());
        assertEquals(member2.getId(), response.get(1).getRoomId());
    }

    @Test
    void leaveRoom_shouldReturnConfirmationMessage() {
        // Given
        Long roomId = 1L;
        Long memberId = 1L;
        LeaveRoomMemberRequestDto requestDto = new LeaveRoomMemberRequestDto(roomId, memberId);
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto(
                "Test Room",
                "test-room-url",
                null,
                "Room detail",
                1,
                10,
                null,
                false
        );
        Room room = new Room(roomId, createRoomRequestDto, memberId);
        Member member = new Member(memberId, "test@example.com", "password", "nickname", null);
        RoomMember roomMember = new RoomMember(room, member);

        // Mocking
        when(roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId)).thenReturn(Optional.of(roomMember));

        // When
        String response = roomMemberService.leaveRoom(requestDto);

        // Then
        assertEquals("스터디룸을 떠납니다.", response);
    }
}
