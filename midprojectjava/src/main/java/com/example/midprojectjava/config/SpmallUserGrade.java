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
	    
	 // 권한 반환 메서드
	    public static String getRoleByGrade(int grade) {
	        switch (grade) {
	            case 0: return ADMIN.getValue();   // 예: 관리자
	            case 1: return SELLER.getValue();    // 예: 판매자
	            case 2: return GOLD.getValue();  // 예: 골드
	            case 3: return SILVER.getValue();  // 예: 실버
	            case 4: return BRONZE.getValue();  // 예: 브론즈
	            default: return BRONZE.getValue();    // 기본값
	        }
	    }
}
