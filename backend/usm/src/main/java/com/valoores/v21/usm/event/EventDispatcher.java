package com.valoores.v21.usm.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Handles the communication with the Event Bus.
 */

@Component
public class EventDispatcher {

    private RabbitTemplate rabbitTemplate;

    // The exchange to use to send anything related to the UsmUser
    private String usmExchange;

    // The routing key to use to send this particular event
    private String usmRoutingKey;

    EventDispatcher(final RabbitTemplate rabbitTemplate,
                    @Value("${usm.exchange}") final String usmExchange,
                    @Value("${usm.log.key}") final String usmRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.usmExchange = usmExchange;
        this.usmRoutingKey = usmRoutingKey;
    }

    public void send(final UsmEntityLogEvent usmEntityLogEvent) {
    	
    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    	System.out.println("Sending event....");
    	System.out.println("exchange = " + usmExchange);
    	System.out.println("routing key = " + usmRoutingKey);
    	System.out.println("log event = " + usmEntityLogEvent.toString());
        rabbitTemplate.convertAndSend(
        		usmExchange,
        		usmRoutingKey,
        		usmEntityLogEvent);
        
        System.out.println("Event sent!");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
}
