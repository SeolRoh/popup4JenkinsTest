package com.popup.image.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.popup.image.Domain.Images;
import com.popup.image.repository.ImagesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	private final ImagesRepository imagesRepository;
	public void saveImageUrls(List<String> urls, String id){
		if(imagesRepository.findById(id).isPresent()){
			System.out.println("url already exists in mongdb");
		} else {
			Images images = new Images();
			images.setId(id);
			images.setUrls(urls);
			imagesRepository.save(images);
			System.out.println(" MongoDB Image Url Uploaded Completely");
		}
	}
}
