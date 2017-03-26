package com.eduportal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@NoArgsConstructor
public class SavedFile {
	
	@Id
	private String id;
	private String filename;
	
}
