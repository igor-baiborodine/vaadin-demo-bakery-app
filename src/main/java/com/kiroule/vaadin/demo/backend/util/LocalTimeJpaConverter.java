package com.kiroule.vaadin.demo.backend.util;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA does not know how to handle Java 8 java.time.LocalTime so this converts
 * LocalTime into format it can handle.
 */
@Converter(autoApply = true)
public class LocalTimeJpaConverter implements AttributeConverter<LocalTime, Time> {

	@Override
	public Time convertToDatabaseColumn(LocalTime time) {
		if (time == null) {
			return null;
		}

		return Time.valueOf(time);
	}

	@Override
	public LocalTime convertToEntityAttribute(Time time) {
		if (time == null) {
			return null;
		}

		return time.toLocalTime();
	}

}
