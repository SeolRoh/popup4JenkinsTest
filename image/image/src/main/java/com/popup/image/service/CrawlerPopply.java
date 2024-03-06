package com.popup.image.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlerPopply {
	private static final String MAIN_PAGE_URL = "https://www.popply.co.kr/popup/1370";
	private final WebDriver driver = WebDriverManager.initChromeDriver();
	//private final HashMap<Integer, String> detailPageUrls = new HashMap<>();

	private final S3Uploader s3Uploader;
	private final ImageService imageService;


	// 0. 크롤링 시작
	public void startCrawling() throws IOException, InterruptedException {
		setCrawler();
		getMainPageInfos();
	}

	// 1. 크롤러 객체에 url 할당
	private void setCrawler() {
		// 가장 상위 페이지에서 정보를 가져옴
		driver.get(MAIN_PAGE_URL);
	}

	// 2. 크롤링 할 정보 추출
	private void getMainPageInfos() throws IOException, InterruptedException {
		//var mainPageInfos = driver.findElement(By.className("popupdetail-title-info"));
		//var mainPageInfos = driver.findElement(By.className("slide-img-wrap")).getText();
		var mainPageInfos = driver.findElements(By.className("slide-content"));
		//var mainPageInfos = driver.findElement(By.className("slide-content")).findElement(By.tagName("img")).getAttribute("src");
			//.By.className("popupdetail-title-info").getText();
		setPopUpStoreInfos(mainPageInfos);
	}

	// 3. 데이터 가공
	private void setPopUpStoreInfos(List<WebElement> infos) throws IOException, InterruptedException {
		List<String> urls = new ArrayList<>();
		try {
			for (int i = 0; i < infos.size()-1; i++) {
				String name = infos.get(i)
					.findElement(By.className("slide-img-wrap"))
					.findElement(By.tagName("img"))
					.getAttribute("src");

				urls.add(name);
			}
		} catch(Exception e){
			System.out.println("Exception!");
		}
		setSaveS3ImageUrl(urls);
	}

	// 4. 상세 url 하나씩 접근
	// private void getDetailInfos() {
	// 	for (var detailPageUrlMap : detailPageUrls.entrySet()) {
	// 		driver.get(detailPageUrlMap.getValue());
	// 		setDetailInfos(detailPageUrlMap.getKey());
	// 	}
	// }

	// 5. 이미지 S3 저장
	private void setSaveS3ImageUrl(List<String> urls) throws InterruptedException {
		String publicUrl = "";
		List<String> publicUrls = new ArrayList<>();
		for (int i = 0; i < urls.size(); i++){ //String url : urls) {
			publicUrl = s3Uploader.downloadAndUploadS3(urls.get(i), 1370,i);
			publicUrls.add(publicUrl);
			Thread.sleep(3000);
			// 다른 클래스 불러오면서  Cannot invoke "com.popup.image.service.S3Uploader.uploadS3(String)" because "this.s3Uploader" is null
		}
		System.out.println("Start MongoDB Image Url Upload.");
		imageService.saveImageUrls(publicUrls,"1370");
		System.out.println("Finish MongoDB Image Url Uploaded.");
	}
}
