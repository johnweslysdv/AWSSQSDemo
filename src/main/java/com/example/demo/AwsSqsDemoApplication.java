package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextResourceLoaderAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.mail.MailSenderAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.messaging.MessagingAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.jdbc.AmazonRdsDatabaseAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchExportAutoConfiguration.class})
@EnableContextInstanceData
@ImportResource("classpath:/aws-config.xml")
@EnableSqs
public class AwsSqsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsSqsDemoApplication.class, args);
	}

}
