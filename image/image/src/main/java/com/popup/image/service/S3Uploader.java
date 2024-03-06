package com.popup.image.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class S3Uploader {

	@Autowired
	private S3Client s3Client;

	@Value("${aws.s3.bucket}")
	public String bucket;

	public String downloadAndUploadS3(String url, Integer page, Integer numbering){
		String objectKey = page+"/image_"+numbering+".jpg";
		String publicUrl = ""; // 업로드된 객체의 URL을 저장할 변수

		try {
			// 이미지를 임시 파일로 다운로드
			Path tempFile = Files.createTempFile(null, ".tmp");
			try (InputStream in = new URL(url).openStream()) {
				Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
			}

			// S3에 업로드
			try (S3Client s3 = S3Client.builder()
				.region(Region.of("ap-northeast-2"))
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build()) {

				PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucket)
					.key(objectKey)
					.build();

				s3.putObject(putObjectRequest, RequestBody.fromFile(tempFile));

				// 업로드된 객체의 URL을 가져옴
				publicUrl = s3.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(objectKey).build()).toExternalForm();

				System.out.println("S3 Image uploaded successfully. url:"+publicUrl);
			}

			// 임시 파일 삭제
			Files.delete(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return publicUrl;
	}
}