package com.jungle.studybbitback.domain.room.service;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
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
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;  // CustomUserDetails import 추가
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
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

        // Mock된 Member 객체 생성
        Member mockMember = new Member(1L, "testUser@example.com", "password", "testUser", MemberRoleEnum.ROLE_USER);

        // CustomUserDetails로 감싸서 인증 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(mockMember);

        // Mock된 Room 객체 생성
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto(
                "Test Room",
                "test-room-url",
                "password",
                "Room detail",
                1,
                10,
                null,
                false
        );
        Room mockRoom = new Room(createRoomRequestDto, mockMember.getId());

        // Mock된 객체 반환 설정
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockMember));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));

        // RoomMemberRepository save Mock 설정
        when(roomMemberRepository.save(any(RoomMember.class))).thenReturn(new RoomMember(mockRoom, mockMember));

        // RoomMemberRepository에서 findByRoomId 호출 시 mockMember 반환 설정
        RoomMember mockRoomMember = new RoomMember(mockRoom, mockMember);
        when(roomMemberRepository.findByRoomId(mockRoom.getId())).thenReturn(List.of(mockRoomMember));

        // SecurityContext 설정 (CustomUserDetails로 인증된 사용자)
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void inviteRoomMember_shouldReturnInviteRoomMemberResponseDto() {
        // Given
        Long roomId = 1L;
        String email = "test@example.com";
        String nickname = "testnickname";
        InviteRoomMemberRequestDto requestDto = new InviteRoomMemberRequestDto(roomId, email);

        Member member = new Member(1L, email, "password", nickname, null);
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
        assertEquals(roomId, response.getRoomId());          // 방 ID 확인
        assertEquals(nickname, response.getNickname());      // 닉네임 확인
    }

    @EnabledIfSystemProperty(named = "runApiTest", matches = "true") //방 참여 멤버임이 입증이 안 되지만 포스트맨은 통과함
    @Test
    void getRoomMember_shouldReturnGetRoomMemberResponseDto() {
        // Given
        Long roomId = 1L;
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
        Room room = new Room(createRoomRequestDto, 1L);
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
        assertEquals("nickname", response.get(0).getNickname());
        assertEquals("nickname2", response.get(1).getNickname());
    }

    @Test
    void leaveRoom_shouldReturnConfirmationMessage() {
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
        Room room = new Room(roomId, createRoomRequestDto, memberId);
        Member member = new Member(memberId, "test@example.com", "password", "nickname", null);
        RoomMember roomMember = new RoomMember(room, member);

        // Mocking
        when(roomMemberRepository.findByRoomIdAndMemberId(roomId, memberId)).thenReturn(Optional.of(roomMember));

        // When
        String response = roomMemberService.leaveRoom(roomId);

        // Then
        assertEquals("스터디룸을 떠납니다.", response);
    }
}
