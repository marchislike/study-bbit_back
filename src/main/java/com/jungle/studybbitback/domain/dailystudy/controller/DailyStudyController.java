package com.jungle.studybbitback.domain.dailystudy.controller;

import com.jungle.studybbitback.domain.dailystudy.dto.GetDailyStudyByPeriodResponseDto;
import com.jungle.studybbitback.domain.dailystudy.dto.GetDailyStudyResponseDto;
import com.jungle.studybbitback.domain.dailystudy.dto.WriteStudyRequestDto;
import com.jungle.studybbitback.domain.dailystudy.dto.WriteStudyResponseDto;
import com.jungle.studybbitback.domain.dailystudy.service.DailyStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
	@GetMapping("/{studyDate}")
	public ResponseEntity<GetDailyStudyResponseDto> getStudyByDate(@PathVariable("studyDate") LocalDate studyDate) {
		GetDailyStudyResponseDto response = dailyStudyService.getStudyByDate(studyDate);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/year/{studyYear}")
	public ResponseEntity<List<GetDailyStudyResponseDto>> getStudyByYear(
			@PathVariable("studyYear") Integer year) {
		List<GetDailyStudyResponseDto> response = dailyStudyService.getStudyByYear(year);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/period/{memberId}/{startDate}/{endDate}")
	public ResponseEntity<GetDailyStudyByPeriodResponseDto> getStudyByPeriod(
			@PathVariable("memberId") Long memberId,
			@PathVariable("startDate") LocalDate startDate,
			@PathVariable("endDate") LocalDate endDate) {
		GetDailyStudyByPeriodResponseDto responseDto = dailyStudyService.getStudyByPeriod(memberId, startDate, endDate);
		return ResponseEntity.ok(responseDto);
	}
}
