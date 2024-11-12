package com.jungle.studybbitback.domain.posting.controller;

import com.jungle.studybbitback.domain.posting.dto.posting.*;
import com.jungle.studybbitback.domain.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posting")
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    @PostMapping()
    public AddPostingResponseDto addPosting(@RequestBody AddPostingRequestDto requestDto) {
        return postingService.addPosting(requestDto);
    }

    @GetMapping("/all")
    public List<GetPostingResponseDto> getPostingAll() {
        return postingService.getPostingAll();
    }

    @GetMapping("/{id}")
    public GetPostingResponseDto getPosting(@PathVariable("id") Long id) {
        return postingService.getPostingById(id);
    }

    @PostMapping("/{id}")
    public UpdatePostingResponseDto updatePosting(@PathVariable("id") Long id,
												  @RequestBody UpdatePostingRequestDto requestDto) {
        return postingService.updatePosting(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public String deletePosting(@PathVariable("id") Long id, @RequestBody DeletePostingRequestDto requestDto) {
        return postingService.deletePosting(id, requestDto);
    }


}
