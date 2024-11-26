package com.jungle.studybbitback.domain.dailystudy.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WriteStudyRequestDto {
	private LocalDateTime start;
	private LocalDateTime end;
}
