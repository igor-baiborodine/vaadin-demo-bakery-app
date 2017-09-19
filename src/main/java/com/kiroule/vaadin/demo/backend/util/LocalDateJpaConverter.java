package com.kiroule.vaadin.demo.backend.util;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA does not know how to handle Java 8 java.time.LocalDate so this converts
 * LocalDate into format it can handle.
 */
@Converter(autoApply = true)
public class LocalDateJpaConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate date) {
		if (date == null) {
			return null;
		}

		return Date.valueOf(date);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}

		return date.toLocalDate();
	}

}
