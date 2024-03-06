package com.popup.image.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AmazonS3Config {

	@Value("${aws.credentials.accessKey}")
	private String accessKey;

	@Value("${aws.credentials.secretKey}")
	private String secretKey;

	@Value("${aws.region.static}")
	private String region;

	@Bean
	public S3Client s3Client() {
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

		return S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
			.build();
	}
}
