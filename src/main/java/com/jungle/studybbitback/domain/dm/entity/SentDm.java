package com.jungle.studybbitback.domain.dm.entity;

import com.jungle.studybbitback.common.entity.CreatedTimeEntity;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SentDm extends CreatedTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sent_dm_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	//@ManyToOne
	@JoinColumn(name ="sender_id")
	private Member sender;

	@ManyToOne(fetch = FetchType.LAZY)
	//@ManyToOne
	@JoinColumn(name ="receiver_id")
	private Member receiver;

	private String content;

	public SentDm(SendDmRequestDto request, Member sender, Member receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = request.getContent();
	}
}
