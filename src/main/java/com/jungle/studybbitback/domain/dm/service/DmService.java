package com.jungle.studybbitback.domain.dm.service;

import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmResponseDto;
import com.jungle.studybbitback.domain.dm.entity.Dm;
import com.jungle.studybbitback.domain.dm.repository.DmRepository;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import com.jungle.studybbitback.notification.entity.Notification;
import com.jungle.studybbitback.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

	@Transactional
	public String deleteDm(Long dmId) {
		Dm dm = dmRepository.findById(dmId).orElseThrow(
				() -> new EntityNotFoundException("쪽지를 찾을 수 없습니다.")
		);

		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		if (dm.getReceiver().getId() != memberId) {
			throw new AccessDeniedException("수신자만이 삭제 가능합니다.");
		}

		dmRepository.delete(dm);

		return dmId + "번 쪽지가 삭제되었습니다.";
	}

	@Transactional
	public String deleteAllDm() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		dmRepository.deleteByReceiverId(memberId);

		return "모든 쪽지가 삭제되었습니다.";
	}
}
