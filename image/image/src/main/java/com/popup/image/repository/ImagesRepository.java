package com.popup.image.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.popup.image.Domain.Images;

public interface ImagesRepository extends MongoRepository<Images, String> {
}
