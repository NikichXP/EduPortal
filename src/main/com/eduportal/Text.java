package com.eduportal;

import lombok.Data;

@Data
public class Text {

	private String value;

	public Text(String text) {
		this.value = text;
	}

}
