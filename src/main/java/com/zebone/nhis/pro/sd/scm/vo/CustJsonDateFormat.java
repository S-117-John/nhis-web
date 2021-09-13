package com.zebone.nhis.pro.sd.scm.vo;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustJsonDateFormat extends JsonDeserializer<Date>{

	private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public Date deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		String text = arg0.getText();
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    return this.dateFormat.parse(text);
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    return this.datetimeFormat.parse(text);
                } else {
                    throw new IllegalArgumentException("Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException exception = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                exception.initCause(ex);
                throw exception;
            }
        } else {
            return null;
        }
	}

}
