package com.jungle.studybbitback.domain.dm.service;

import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmResponseDto;
import com.jungle.studybbitback.domain.dm.entity.Dm;
import com.jungle.studybbitback.domain.dm.repository.DmRepository;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DmService {

	private final MemberRepository memberRepository;
	private final DmRepository dmRepository;
	private final NotificationService notificationService;

	@Transactional
	public SendDmResponseDto sendDm(SendDmRequestDto request) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long senderId = userDetails.getMemberId();
		Long receiverId = request.getReceiverId();
		Member sender = memberRepository.findById(senderId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 송신자입니다."));

		Member receiver = memberRepository.findById(receiverId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수신자입니다."));

		Dm dm = new Dm(request, sender, receiver);
		Dm savedDm = dmRepository.saveAndFlush(dm);

		// 상대방에게 알림 전송
		notificationService.notifyDm(receiverId, sender, receiver);
		log.info("dm 전송 완료!");
		return new SendDmResponseDto(savedDm);
	}

	public Page<GetDmResponseDto> getReceivedDm(Pageable pageable) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return dmRepository.findByReceiverId(userDetails.getMemberId(), pageable).map(GetDmResponseDto::new);
	}

	public Page<GetDmResponseDto> getSentDm(Pageable pageable) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return dmRepository.findBySenderId(userDetails.getMemberId(), pageable).map(GetDmResponseDto::new);
	}
}
