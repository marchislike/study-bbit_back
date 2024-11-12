package com.jungle.studybbitback.posting.service;

import com.jungle.studybbitback.posting.dto.posting.*;
import com.jungle.studybbitback.posting.entity.Posting;
import com.jungle.studybbitback.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
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

        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        String bcryptedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        Posting posting = new Posting(requestDto, loginName, bcryptedPassword);
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

        String bcryptedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());
        if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), searchedPosting.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        String SessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!searchedPosting.getUsername().equals(SessionUsername)) {
            throw new BadCredentialsException("작성자만 삭제/수정할 수 있습니다.");
        }

        searchedPosting.updatePosting(requestDto);

        return new UpdatePostingResponseDto(searchedPosting);
    }

    @Transactional
    public String deletePosting(Long id, DeletePostingRequestDto requestDto) {

        Posting searchedPosting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        String bcryptedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());
        if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), searchedPosting.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        String SessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!searchedPosting.getUsername().equals(SessionUsername)) {
            throw new BadCredentialsException("작성자만 삭제/수정할 수 있습니다.");
        }

        postingRepository.deleteById(id);

        return "success";
    }
}
