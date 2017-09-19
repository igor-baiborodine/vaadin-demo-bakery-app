package com.kiroule.vaadin.demo.ui.util;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class DateTimeFormatter implements Serializable {

	/**
	 * Format the given local time using the given locale.
	 *
	 * @param dateTime
	 *            the date and time to format
	 * @param locale
	 *            the locale to use to determine the format
	 * @return a formatted string
	 */
	public String format(LocalDateTime dateTime, Locale locale) {
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale);
		return dateTime.format(formatter);
	}

}
