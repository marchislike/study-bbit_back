package com.jungle.studybbitback.domain.dm.controller;

import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmResponseDto;
import com.jungle.studybbitback.domain.dm.service.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dm")
@RequiredArgsConstructor
public class DmController {

	private final DmService dmService;

	// 디엠 송신
	@PostMapping()
	public ResponseEntity<SendDmResponseDto> sendDm(@RequestBody SendDmRequestDto request) {
		SendDmResponseDto response = dmService.sendDm(request);
		return ResponseEntity.ok(response);
	}

	// 내가 받은 디엠 조회
	@GetMapping("/received")
	public ResponseEntity<Page<GetDmResponseDto>> getReceivedDm(
			@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
			Pageable pageable) {
		Page<GetDmResponseDto> response = dmService.getReceivedDm(pageable);
		return ResponseEntity.ok(response);
	}

	// 내가 보낸 디엠 조회
	@GetMapping("/sent")
	public ResponseEntity<Page<GetDmResponseDto>> getSentDm(
			@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
			Pageable pageable) {
		Page<GetDmResponseDto> response = dmService.getSentDm(pageable);
		return ResponseEntity.ok(response);
	}

	// 보낸 디엠 1건 삭제
	@DeleteMapping("/sent/{sentDmId}")
	public ResponseEntity<String> deleteSentDm(@PathVariable("sentDmId") Long sentDmId) {

		String response = dmService.deleteSentDm(sentDmId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 보낸 디엠 전체 삭제
	@DeleteMapping("/sent")
	public ResponseEntity<String> deleteAllSentDm() {

		String response = dmService.deleteAllSentDm();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 받은 디엠 1건 삭제
	@DeleteMapping("/received/{receivedDmId}")
	public ResponseEntity<String> deleteReceivedDm(@PathVariable("receivedDmId") Long receivedDmId) {

		String response = dmService.deleteReceivedDm(receivedDmId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// 받은 디엠 전체 삭제
	@DeleteMapping("/received")
	public ResponseEntity<String> deleteAllReceivedDm() {

		String response = dmService.deleteAllReceivedDm();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
