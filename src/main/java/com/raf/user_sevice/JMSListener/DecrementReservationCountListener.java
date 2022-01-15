package com.raf.user_sevice.JMSListener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raf.user_sevice.dto.IncrementReservationCountDto;
import com.raf.user_sevice.service.impl.UserService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
public class DecrementReservationCountListener {

    private ObjectMapper objectMapper;
    private UserService userService;

    public DecrementReservationCountListener(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @JmsListener(destination = "${destination.decrement.reservation.count}", concurrency = "5-10")
    public void decrementReservationCount(Message message)throws JMSException, JsonParseException, JsonMappingException, IOException {
        String json = ((TextMessage)message).getText();
        IncrementReservationCountDto dto = objectMapper.readValue(json, IncrementReservationCountDto.class);
        userService.decrementReservationCountForUser(dto);
    }
}
