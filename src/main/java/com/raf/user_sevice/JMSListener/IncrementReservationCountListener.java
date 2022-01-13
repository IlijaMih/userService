package com.raf.user_sevice.JMSListener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raf.user_sevice.dto.IncrementReservationCountDto;
import com.raf.user_sevice.service.impl.UserService;

@Component
public class IncrementReservationCountListener {

	private ObjectMapper objectMapper;
	private UserService userService;
	
	public IncrementReservationCountListener(ObjectMapper objectMapper, UserService userService) {
		super();
		this.objectMapper = objectMapper;
		this.userService = userService;
	}




	@JmsListener(destination = "${destination.increment.reservation.count}", concurrency = "5-10")
	public void incrementReservationCount(Message message) throws JMSException, JsonParseException, JsonMappingException, IOException {
		String json = ((TextMessage)message).getText();
		IncrementReservationCountDto dto = objectMapper.readValue(json, IncrementReservationCountDto.class);
		System.out.println(dto);
		userService.incrementReservationCountForUser(dto);
	}
	
}
