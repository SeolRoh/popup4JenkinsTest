package com.popup.image.Domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "images")
@Getter
@Setter
@NoArgsConstructor
public class Images {

	@Id
	private String id;
	private List<String> urls;
}
