package com.example.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

@RestController
public class SqsDemoController {

	private static final Logger logger = LoggerFactory.getLogger(SqsDemoController.class);

	// @Autowired
	// QueueMessagingTemplate template;

	@Autowired
	JmsTemplate template;

	@Autowired
	SQSConnectionFactory sqsJmsConnectionFactory;

	Session session = null;

	@RequestMapping(value = "/sendmessage/{message}", method = RequestMethod.GET)
	public ResponseEntity sendMessage(@PathVariable("message") String msg) {

		
		TextMessage message = null;
		String msgID = null;
		try {

			SQSConnection connection = sqsJmsConnectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("MySampleQueue");
			MessageProducer producer = session.createProducer(queue);
			// Create the text message
			message = session.createTextMessage(msg);
			// Send the message
			producer.send(message);
			System.out.println("JMS Message " + message.getJMSMessageID());
			logger.info("JMS Message " + message.getJMSMessageID());
			msgID = message.getJMSMessageID();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>("message sent JSM ID: "+ msgID,org.springframework.http.HttpStatus.OK);

	}

	

	@SqsListener(value = "https://sqs.ap-south-1.amazonaws.com/442302240019/MySampleQueue",deletionPolicy = SqsMessageDeletionPolicy.NEVER)
	public void receive(@Payload String message, final Acknowledgment acknowledgment,@Headers Map<String, String> header) {
		
		
		logger.info("===========================the message received" + message);
		logger.info("=======================the header value"+header.get("MessageId"));
		logger.info("=======================the header value"+header.get("ReceiptHandle"));
		logger.info("=======================the header value"+header.get("MD5OfMessageAttributes"));
		logger.info("=======================the header value"+header.get("MD5OfBody"));
		logger.info("=======================the header value"+header.get("Body"));
		
		 // deleting message from queue after processing.
		 acknowledgment.acknowledge();
		
	}

	@GetMapping(value = "/")
	public final String hola() throws UnknownHostException {
		return "hello from Microservice the Incoming Host IP:" + InetAddress.getLocalHost().getHostAddress() + "\n";
	}

}
