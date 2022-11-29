package com.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthVO {
	
	private String loginId; 
	private String auth;
	
	public AuthVO(String loginId) {
		this.loginId = loginId;
	}
}
