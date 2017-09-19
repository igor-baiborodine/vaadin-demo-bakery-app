package com.kiroule.vaadin.demo.backend.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA does not know how to handle Java 8 java.time.LocalDateTime so this
 * converts LocalDateTime into format it can handle.
 */
@Converter(autoApply = true)
public class LocalDateTimeJpaConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}

		return Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp time) {
		if (time == null) {
			return null;
		}

		return LocalDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC);
	}

}
