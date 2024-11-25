//package com.jungle.studybbitback.domain.room.service;
//
//import com.jungle.studybbitback.domain.member.entity.Member;
//import com.jungle.studybbitback.domain.member.entity.MemberRoleEnum;
//import com.jungle.studybbitback.domain.member.repository.MemberRepository;
//import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository; // 추가
//import com.jungle.studybbitback.domain.room.dto.room.CreateRoomRequestDto;
//import com.jungle.studybbitback.domain.room.dto.room.CreateRoomResponseDto;
//import com.jungle.studybbitback.domain.room.entity.Room;
//import com.jungle.studybbitback.domain.room.respository.RoomRepository;
//import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class RoomServiceTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(RoomServiceTest.class);
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @Mock
//    private RoomMemberRepository roomMemberRepository; // 추가
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @InjectMocks
//    private RoomService roomService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock된 Member 객체 생성
//        Member mockMember = new Member(1L,"testUser@example.com", "password", "testUser", MemberRoleEnum.ROLE_USER);
//
//        logger.info("Member ID: {}", mockMember.getId());
//
//        // Mock된 사용자 인증 설정
//        CustomUserDetails userDetails = new CustomUserDetails(mockMember);
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        // SecurityContextHolder의 인증 정보 확인
//        logger.info("Authenticated User ID from SecurityContextHolder: {}", userDetails.getMemberId());
//
//        // Mock된 MemberRepository에서 findById 메소드 호출시 mockMember 반환
//        when(memberRepository.findById(1L)).thenReturn(java.util.Optional.of(mockMember));
//
//        // CreateRoomRequestDto 객체 생성
//        CreateRoomRequestDto requestDto = CreateRoomRequestDto.builder()
//                .name("Test Room")
//                .password("password")
//                .detail("Room Detail")
//                .maxParticipants(10)
//                .profileImageUrl("image.jpg")
//                .isPrivate(false)
//                .build();
//
//        // 테스트용 Room 객체 생성
//        Room mockRoom = new Room(1L, requestDto, 1L); // 테스트용 생성자 호출
//        when(roomRepository.saveAndFlush(any(Room.class))).thenReturn(mockRoom); // save 호출 시 mockRoom 반환
//
//    }
//
//    @Test
//    void createRoom_shouldReturnRoomResponseDto() {
//        // Given: CreateRoomRequestDto 객체 생성
//        CreateRoomRequestDto requestDto = CreateRoomRequestDto.builder()
//                .name("Room1")
//                .password("password")
//                .detail("Test Room")
//                .maxParticipants(10)
//                .profileImageUrl("image.jpg")
//                .isPrivate(false)
//                .build();
//
//        logger.info("Testing createRoom with request DTO: {}", requestDto);
//
//        // Mock 설정된 Room 객체
//        Room room = new Room(requestDto, 1L); // 예시용 Room 객체 생성
//        when(roomRepository.saveAndFlush(any(Room.class))).thenReturn(room);  //!! saveAndFlush에 대한 Mock 추가
//        logger.info("Mocked roomRepository to return Room: {}", room);
//
//        // When: roomService.createRoom 호출
//        CreateRoomResponseDto response = roomService.createRoom(requestDto);
//
//        // Then: response 필드값 확인
//        assertNotNull(response); // Null이 아닌지 확인
//        assertEquals("Room1", response.getName());
//    }
//}