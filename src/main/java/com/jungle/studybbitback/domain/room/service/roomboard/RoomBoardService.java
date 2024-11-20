package com.jungle.studybbitback.domain.room.service.roomboard;

import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.dto.roomboard.CreateRoomBoardRequestDto;
import com.jungle.studybbitback.domain.room.dto.roomboard.CreateRoomBoardResponseDto;
import com.jungle.studybbitback.domain.room.dto.roomboard.GetRoomBoardDetailResponseDto;
import com.jungle.studybbitback.domain.room.dto.roomboard.GetRoomBoardResponseDto;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.entity.roomboard.RoomBoard;
import com.jungle.studybbitback.domain.room.respository.RoomMemberRepository;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.domain.room.respository.roomboard.RoomBoardRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomBoardService {

    private final RoomBoardRepository roomBoardRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateRoomBoardResponseDto createRoomBoard(CreateRoomBoardRequestDto requestDto) {

        // 로그인된 사용자의 Member 객체를 가져오기
        Member member = getAuthenticatedMember();

        // 스터디룸 정보 조회
        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스터디룸이 존재하지 않습니다."));

        // 해당 사용자가 스터디룸 멤버인지 확인
        if (roomMemberRepository.findByRoomIdAndMemberId(requestDto.getRoomId(), member.getId()).isEmpty()) {
            throw new IllegalArgumentException("해당 스터디룸에 가입된 사용자만 게시글을 작성할 수 있습니다.");
        }

        // 게시글 생성
        RoomBoard roomBoard = new RoomBoard(room, requestDto.getTitle(), requestDto.getContent(), member.getId());
        roomBoardRepository.save(roomBoard);

        // 작성자의 닉네임 조회
        String createdByNickname = memberRepository.findById(member.getId())
                .map(Member::getNickname)
                .orElse("알 수 없음");

        // DTO 변환 후 반환
        return CreateRoomBoardResponseDto.from(roomBoard, createdByNickname);
    }

    // 해당 스터디룸 게시글 전체 조회
    @Transactional(readOnly = true)
    public Page<GetRoomBoardResponseDto> getRoomBoards(Long roomId, Pageable pageable) {
        return roomBoardRepository.findByRoomId(roomId, pageable)
                .map(roomBoard -> {
                    String createdByNickname = roomBoard.getCreatedByNickname(memberRepository);
                    return GetRoomBoardResponseDto.from(roomBoard, createdByNickname);
                });
    }


    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public GetRoomBoardDetailResponseDto getRoomBoardDetail(Long roomBoardId) {
        return roomBoardRepository.findById(roomBoardId)
                .map(roomBoard -> {
                    String createdByNickname = roomBoard.getCreatedByNickname(memberRepository);
                    return GetRoomBoardDetailResponseDto.from(roomBoard, createdByNickname);
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    private Member getAuthenticatedMember() {
        return memberRepository.findById(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }


}
