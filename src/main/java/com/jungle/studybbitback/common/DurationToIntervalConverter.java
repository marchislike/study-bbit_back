package com.jungle.studybbitback.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationToIntervalConverter implements AttributeConverter<Duration, String> {

	@Override
	public String convertToDatabaseColumn(Duration duration) {
		if (duration == null) {
			return null;
		}
		long seconds = duration.getSeconds();
		long absSeconds = Math.abs(seconds);
		return String.format(
				"%d:%02d:%02d",
				absSeconds / 3600,                // Hours
				(absSeconds % 3600) / 60,        // Minutes
				absSeconds % 60                  // Seconds
		);
	}

	@Override
	public Duration convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return Duration.ZERO;
		}

		try {
			// Handle PostgreSQL INTERVAL format (e.g., "01:59:59")
			String[] timeParts = dbData.split(":");
			long hours = Long.parseLong(timeParts[0]);
			long minutes = Long.parseLong(timeParts[1]);
			long seconds = Long.parseLong(timeParts[2]);
			return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse INTERVAL value: " + dbData, e);
		}
	}
}
