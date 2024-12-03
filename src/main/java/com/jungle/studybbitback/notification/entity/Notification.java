package com.jungle.studybbitback.notification.entity;

import com.jungle.studybbitback.common.entity.CreatedTimeEntity;
import com.jungle.studybbitback.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends CreatedTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name ="receiver_id")
	private Member receiver;

	private String content;

	private String url;

	@Column(nullable = false)
	private Boolean isRead;

	public Notification(Member receiver, String content, String url) {
		this.receiver = receiver;
		this.content = content;
		this.url = url;
		this.isRead = false;
	}

	public void updateIsRead() {
		this.isRead = true;
	}
}
