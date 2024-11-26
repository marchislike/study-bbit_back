package com.jungle.studybbitback.domain.dailystudy.controller;

import com.jungle.studybbitback.domain.dailystudy.dto.GetDailyStudyRequestDto;
import com.jungle.studybbitback.domain.dailystudy.dto.GetDailyStudyResponseDto;
import com.jungle.studybbitback.domain.dailystudy.dto.WriteStudyRequestDto;
import com.jungle.studybbitback.domain.dailystudy.dto.WriteStudyResponseDto;
import com.jungle.studybbitback.domain.dailystudy.service.DailyStudyService;
import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily-study")
@RequiredArgsConstructor
public class DailyStudyController {

	private final DailyStudyService dailyStudyService;
	
	// 공부시간 저장
	@PostMapping()
	public ResponseEntity<WriteStudyResponseDto> saveDailyStudy(@RequestBody WriteStudyRequestDto request) {
		WriteStudyResponseDto response = dailyStudyService.saveDailyStudy(request);
		return ResponseEntity.ok(response);
	}

	// 모든 공부시간 조회
	@GetMapping()
	public ResponseEntity<Page<GetDailyStudyResponseDto>> getStudyAll(
			@PageableDefault(size = 10, sort = "studyDate", direction = Sort.Direction.DESC)
			Pageable pageable) {
		Page<GetDailyStudyResponseDto> response = dailyStudyService.getDailyStudyAll(pageable);
		return ResponseEntity.ok(response);
	}

	// 특정 날짜 공부시간 조회
	@GetMapping("/date")
	public ResponseEntity<GetDailyStudyResponseDto> getStudyByDate(@RequestBody GetDailyStudyRequestDto request) {
		GetDailyStudyResponseDto response = dailyStudyService.getStudyByDate(request);
		return ResponseEntity.ok(response);
	}
}
