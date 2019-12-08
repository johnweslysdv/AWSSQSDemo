package com.example.demo;

import javax.jms.ConnectionFactory;

import javax.jms.JMSException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsTemplate;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;

/**
 * @author johnwesly
 *
 */

@Configuration
@ComponentScan(value = "com.example.demo")
public class SqsConfigurations {

	@Value("${cloud.aws.region.static}")
	public String defaultRegion;
	@Value("${cloud.aws.credentials.accessKey}")
	public String testAccessKey;
	@Value("${cloud.aws.credentials.secretKey}")
	public String testSecretKey;
	@Value("${cloud.aws.endpoint.uri}")
	public String queueEndPointUri;

	/*
	 * @Bean public QueueMessagingTemplate getTemplate() { QueueMessagingTemplate
	 * queuMessagingTemplate = null;
	 * 
	 * queuMessagingTemplate = new QueueMessagingTemplate(getSqs()); return
	 * queuMessagingTemplate; }
	 */
	@Bean
	public JmsTemplate getJmsTemplate() {
		JmsTemplate jmsTemplate = null;
		try {
			jmsTemplate = new JmsTemplate(getsqsConnectionFactory());
			jmsTemplate.setDefaultDestinationName("MySampleQueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jmsTemplate;


	}

	public AmazonSQSAsync getSqs() {
		AmazonSQSAsync sqsClient = null;

		sqsClient = AmazonSQSAsyncClientBuilder.standard().withCredentials(awsCredentialsProviderChain())
				.withRegion(Regions.fromName(defaultRegion)).build();
		return new AmazonSQSBufferedAsyncClient(sqsClient);

	}

	@Bean
	AWSCredentialsProviderChain awsCredentialsProviderChain() {
		System.out.println("===========================" + testAccessKey + " " + testSecretKey + " " + queueEndPointUri
				+ " " + defaultRegion);
		return new AWSCredentialsProviderChain(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(testAccessKey, testSecretKey)));
	}

	@Bean
	public SQSConnectionFactory getsqsConnectionFactory() throws JMSException {
		SQSConnectionFactory connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(), getSqs());
		// Create the connection.
		// SQSConnection connection = connectionFactory.createConnection();
		// return connection;
		return connectionFactory;
	}

	

	protected SQSConnectionFactory createStandardSQSConnectionFactory() {
		AmazonSQSAsync sqsClient = getSqs();

		ProviderConfiguration providerConfiguration = new ProviderConfiguration();
		// sqsProperties.getNumberOfMessagesToPrefetch().ifPresent(providerConfiguration::setNumberOfMessagesToPrefetch);

		return new SQSConnectionFactory(providerConfiguration, sqsClient);
	}

	// Queue listener related Beans

	/*
	 * @Autowired public BeanFactory beanFactory; private int maxNumberOfMessages
	 * =5; // Configure the maximum number of messages that should be retrieved
	 * during one // poll to the Amazon SQS system.
	 * 
	 * @Bean public SimpleMessageListenerContainer simpleMessageListenerContainer(
	 * final SimpleMessageListenerContainerFactory
	 * simpleMessageListenerContainerFactory, final QueueMessageHandler
	 * queueMessageHandler) { SimpleMessageListenerContainer
	 * simpleMessageListenerContainer = simpleMessageListenerContainerFactory
	 * .createSimpleMessageListenerContainer();
	 * simpleMessageListenerContainer.setMessageHandler(queueMessageHandler); return
	 * simpleMessageListenerContainer; }
	 * 
	 * @Bean public SimpleMessageListenerContainerFactory
	 * simpleMessageListenerContainerFactory() {
	 * SimpleMessageListenerContainerFactory msgListenerContainerFactory = new
	 * SimpleMessageListenerContainerFactory();
	 * msgListenerContainerFactory.setAmazonSqs(getSqs());
	 * msgListenerContainerFactory.setWaitTimeOut(20);
	 * msgListenerContainerFactory.setMaxNumberOfMessages(maxNumberOfMessages);
	 * return msgListenerContainerFactory; }
	 * 
	 * @Bean public QueueMessageHandler queueMessageHandler(final
	 * QueueMessageHandlerFactory queueMessageHandlerFactory) { return
	 * queueMessageHandlerFactory.createQueueMessageHandler(); }
	 * 
	 * @Bean public QueueMessageHandlerFactory queueMessageHandlerFactory(final
	 * AmazonSQSAsync amazonSqs) { QueueMessageHandlerFactory queueMsgHandlerFactory
	 * = new QueueMessageHandlerFactory();
	 * queueMsgHandlerFactory.setAmazonSqs(amazonSqs);
	 * queueMsgHandlerFactory.setBeanFactory(this.beanFactory); return
	 * queueMsgHandlerFactory; }
	 */

	public String getDefaultRegion() {
		return defaultRegion;
	}

	public void setDefaultRegion(String defaultRegion) {
		defaultRegion = defaultRegion;
	}

	public String getTestAccessKey() {
		return testAccessKey;
	}

	public void setTestAccessKey(String testAccessKey) {
		testAccessKey = testAccessKey;
	}

	public String getTestSecretKey() {
		return testSecretKey;
	}

	public void setTestSecretKey(String testSecretKey) {
		testSecretKey = testSecretKey;
	}

	public String getQueueEndPointUri() {
		return queueEndPointUri;
	}

	public void setQueueEndPointUri(String queueEndPointUri) {
		queueEndPointUri = queueEndPointUri;
	}

}
