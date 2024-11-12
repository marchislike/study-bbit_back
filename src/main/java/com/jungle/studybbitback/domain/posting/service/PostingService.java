package com.jungle.studybbitback.domain.posting.service;

import com.jungle.studybbitback.domain.posting.dto.posting.*;
import com.jungle.studybbitback.domain.posting.entity.Posting;
import com.jungle.studybbitback.domain.posting.repository.PostingRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostingService {

    private final PostingRepository postingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public AddPostingResponseDto addPosting(AddPostingRequestDto requestDto) {

//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long memberId = userDetails.getMemberId();
//        String email = userDetails.getEmail();
//        String role = userDetails.getRole();
//        log.info("잘 가져오나? memberId : {}, email : {}, role : {}", memberId, email, role);

        Posting posting = new Posting(requestDto);
        Posting savedPosting = postingRepository.saveAndFlush(posting);

        return new AddPostingResponseDto(savedPosting);
    }

    public List<GetPostingResponseDto> getPostingAll() {

        List<Posting> PostingList = postingRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        return PostingList.stream()
                .map(GetPostingResponseDto::new)
                .collect(Collectors.toList());
    }

    public GetPostingResponseDto getPostingById(Long id) {

        Posting searchedPosting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        return new GetPostingResponseDto(searchedPosting);
    }

    @Transactional
    public UpdatePostingResponseDto updatePosting(Long id, UpdatePostingRequestDto requestDto) {

        Posting searchedPosting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        if(searchedPosting.getCreatedBy() != memberId) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        searchedPosting.updatePosting(requestDto);

        return new UpdatePostingResponseDto(searchedPosting);
    }

    @Transactional
    public String deletePosting(Long id, DeletePostingRequestDto requestDto) {

        Posting searchedPosting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getMemberId();

        postingRepository.deleteById(id);

        return "success";
    }
}
