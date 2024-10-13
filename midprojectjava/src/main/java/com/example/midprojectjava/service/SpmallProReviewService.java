/**
 * 쇼핑몰 물품 리뷰 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallProReview;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallProReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallProReviewService {
	  private final SpmallProReviewRepository pReviewRepository;

	    public List<SpmallProReview> findBySpmallProduct_Id(Integer id) {
	        List<SpmallProReview> reviewList = this.pReviewRepository.findBySpmallProduct_Id(id);
	        if (!reviewList.isEmpty()) {
	            return reviewList;
	        }
	        throw new DataNotFoundException("review not found for productId: " + id);
	    }

	    public SpmallProReview create(String content, SpmallProduct product, SpmallUser sUser) {
	    	SpmallProReview pReview = new SpmallProReview();
	        pReview.setContent(content);
	        pReview.setCreateDate(LocalDateTime.now());
	        pReview.setSpmallProduct(product);
	        pReview.setSpmallUser(sUser);
	        try {
	            this.pReviewRepository.save(pReview);
	        } catch (Exception e) {
	            System.out.println("리뷰 작성중 문제가 있습니다");
	            e.printStackTrace();
	        }
	        return pReview;
	    }

	    public SpmallProReview findById(Integer id) {
	        Optional<SpmallProReview> byId = this.pReviewRepository.findById(id);
	        if (byId.isPresent()) {
	            return byId.get();
	        }
	        throw new DataNotFoundException("review not found");

	    }

	    public void delete(SpmallProReview review) {

	        this.pReviewRepository.deleteById(review.getId());
	        System.out.println("리뷰 삭제완료");
	    }
}
