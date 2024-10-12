package com.example.midprojectjava.config;

import lombok.Getter;

@Getter
public enum  SpmallUserGrade {
	   ADMIN("ROLE_ADMIN"),
	    GOLD("ROLE_USER"),
	    SILVER("ROLE_USER"),
	    BRONZE("ROLE_USER"),
	    SELLER("ROLE_SELLER");

	    // TODO 롤 구분해야하나 고민
	 SpmallUserGrade(String value) {
	        this.value = value;
	    }

	    private String value;
}
