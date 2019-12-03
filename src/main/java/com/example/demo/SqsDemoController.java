package com.example.demo;

import java.util.concurrent.CountDownLatch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SqsDemoController {
	
    private static final Logger logger = LoggerFactory.getLogger(SqsDemoController.class);


	@Autowired
	QueueMessagingTemplate template;

	@RequestMapping(value = "/sendmessage/{message}", method = RequestMethod.GET)
	public ResponseEntity sendMessage(@PathVariable("message") String msg) {

		template.send("https://sqs.ap-south-1.amazonaws.com/442302240019/MySampleQueue", MessageBuilder.withPayload(msg).build());
		System.out.println("======================================the mesage is " + msg);
		return ResponseEntity.ok("message sent ");

	}
	
	
	CountDownLatch countDownLatch;

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

	
	@SqsListener("MySampleQueue")
	 public void receiveMessage(String message) {
        logger.info("Received message: {}, having SenderId: {}", message);
    }
	
	

}
