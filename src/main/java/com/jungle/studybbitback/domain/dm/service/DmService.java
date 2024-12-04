package com.jungle.studybbitback.domain.dm.service;

import com.jungle.studybbitback.domain.dm.dto.GetDmResponseDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.dm.dto.SendDmResponseDto;
import com.jungle.studybbitback.domain.dm.entity.ReceivedDm;
import com.jungle.studybbitback.domain.dm.entity.SentDm;
import com.jungle.studybbitback.domain.dm.repository.ReceivedDmRepository;
import com.jungle.studybbitback.domain.dm.repository.SentDmRepository;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
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
	private final SentDmRepository sentDmRepository;
	private final ReceivedDmRepository receivedDmRepository;
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

		SentDm sentDm = new SentDm(request, sender, receiver);
		SentDm savedSentDm = sentDmRepository.saveAndFlush(sentDm);

		ReceivedDm receivedDm = new ReceivedDm(request, sender, receiver);
		receivedDmRepository.save(receivedDm);

		// 상대방에게 알림 전송
		notificationService.notifyDm(receiverId, sender, receiver);
		log.info("dm 전송 완료!");
		return new SendDmResponseDto(savedSentDm);
	}

	public Page<GetDmResponseDto> getReceivedDm(Pageable pageable) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return receivedDmRepository.findByReceiverId(userDetails.getMemberId(), pageable).map(GetDmResponseDto::new);
	}

	public Page<GetDmResponseDto> getSentDm(Pageable pageable) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return sentDmRepository.findBySenderId(userDetails.getMemberId(), pageable).map(GetDmResponseDto::new);
	}

	@Transactional
	public String deleteSentDm(Long sentDmId) {
		SentDm sentDm = sentDmRepository.findById(sentDmId).orElseThrow(
				() -> new EntityNotFoundException("쪽지를 찾을 수 없습니다.")
		);

		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		if (sentDm.getSender().getId() != memberId) {
			throw new AccessDeniedException("송신자만이 삭제 가능합니다.");
		}

		sentDmRepository.delete(sentDm);

		return sentDmId + "번 보낸 쪽지가 삭제되었습니다.";
	}

	@Transactional
	public String deleteAllSentDm() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		sentDmRepository.deleteBySenderId(memberId);

		return "모든 보낸 쪽지가 삭제되었습니다.";
	}

	@Transactional
	public String deleteReceivedDm(Long receivedDmId) {
		ReceivedDm receivedDm = receivedDmRepository.findById(receivedDmId).orElseThrow(
				() -> new EntityNotFoundException("쪽지를 찾을 수 없습니다.")
		);

		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		if (receivedDm.getReceiver().getId() != memberId) {
			throw new AccessDeniedException("수신자만이 삭제 가능합니다.");
		}

		receivedDmRepository.delete(receivedDm);

		return receivedDmId + "번 받은 쪽지가 삭제되었습니다.";
	}

	@Transactional
	public String deleteAllReceivedDm() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long memberId = userDetails.getMemberId();

		receivedDmRepository.deleteByReceiverId(memberId);

		return "모든 받은 쪽지가 삭제되었습니다.";
	}
}
