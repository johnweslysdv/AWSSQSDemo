package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
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

	@Bean
	public QueueMessagingTemplate getTemplate() {
		QueueMessagingTemplate queuMessagingTemplate = null;

		queuMessagingTemplate = new QueueMessagingTemplate(getSqs());
		return queuMessagingTemplate;
	}

	public AmazonSQSAsync getSqs() {

		AmazonSQSAsync sqsClient = null;

		sqsClient = AmazonSQSAsyncClientBuilder.standard()
				.withCredentials(awsCredentialsProviderChain()).withRegion(Regions.fromName(defaultRegion)).build();
		return new AmazonSQSBufferedAsyncClient(sqsClient);

	}


	@Bean
	AWSCredentialsProviderChain awsCredentialsProviderChain() {
		System.out.println("===========================" + testAccessKey + " " + testSecretKey + " " + queueEndPointUri
				+ " " + defaultRegion);
		return new AWSCredentialsProviderChain(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(testAccessKey, testSecretKey)));
	}

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
