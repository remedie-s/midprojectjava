/**
 * 쇼핑몰 유틸 서비스입니다
 */
package com.example.midprojectjava.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UtilService {

    public int toSecondOfDay(int day) {

        return 60 * 60 * 24 * day;
    }

    public void setCookie(String key, String value, int expiration, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiration);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MediaType getMediaType(ServletContext servletContext, String filename) {
        try {
            String mimeType = servletContext.getMimeType(filename);
            MediaType mediaType = MediaType.parseMediaType(mimeType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

    }
}
