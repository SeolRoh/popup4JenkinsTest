package com.popup.image.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class WebDriverManager {
	private static WebDriver driver;
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	// chromedriver 설치 경로 입력
	private static final String WEB_DRIVER_PATH = "C:\\Users\\seol\\Downloads\\chromedriver\\chromedriver.exe";

	private WebDriverManager() {
		// private 생성자로 외부에서 인스턴스 생성을 막음
	}

	public static WebDriver initChromeDriver() {
		setDriverOptions();
		driver = new ChromeDriver(getChromeOptions());
		return driver;
	}

	private static void setDriverOptions() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
	}

	private static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		return options;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void quitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}
}
