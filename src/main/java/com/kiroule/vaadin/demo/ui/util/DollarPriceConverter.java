package com.kiroule.vaadin.demo.ui.util;

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class DollarPriceConverter extends StringToIntegerConverter {

	private static final String ERROR_MSG = "Invalid prices, please re-check the value";
	private StringToDoubleConverter doubleConverter = new StringToDoubleConverter(ERROR_MSG);
	private StringToDoubleConverter currencyConverter = new StringToDoubleConverter(ERROR_MSG) {
		@Override
		protected NumberFormat getFormat(Locale locale) {
			return NumberFormat.getCurrencyInstance(Locale.US);
		}
	};

	public DollarPriceConverter() {
		super(ERROR_MSG);
	}

	@Override
	public Result<Integer> convertToModel(String value, ValueContext context) {
		// $1.00 -> 100
		Result<Double> price = currencyConverter.convertToModel(value, context);
		if (price.isError()) {
			// Try without dollar sign
			price = doubleConverter.convertToModel(value, context);
		}
		return price.map(dbl -> dbl == null ? null : (int) (dbl * 100.0));
	}

	@Override
	public String convertToPresentation(Integer value, ValueContext context) {
		// 100 -> $1.00
		double price = (double) value / 100.0;
		return currencyConverter.convertToPresentation(price, context);
	}
}
