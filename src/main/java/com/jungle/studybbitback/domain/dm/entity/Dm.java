package com.jungle.studybbitback.domain.dm.entity;

import com.jungle.studybbitback.common.entity.CreatedEntity;
import com.jungle.studybbitback.domain.dm.dto.SendDmRequestDto;
import com.jungle.studybbitback.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dm extends CreatedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name ="sender_id")
	private Member sender;

	@ManyToOne
	@JoinColumn(name ="receiver_id")
	private Member receiver;

	private String content;

	public Dm(SendDmRequestDto request, Member sender, Member receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = request.getContent();
	}
}
