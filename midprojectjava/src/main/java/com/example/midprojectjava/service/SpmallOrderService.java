/**
 * 쇼핑몰 주문 서비스입니다
 */
package com.example.midprojectjava.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallOrderService {
	private final SpmallOrderRepository sOrderRepository;

    public Page<SpmallOrder> getAllOrder(Pageable pageable) {
        return this.sOrderRepository.findAll(pageable);
    }

    public Page<SpmallOrder> findBySpmallUser_Id(Integer userid, Pageable pageable) {
        Page<SpmallOrder> orders = this.sOrderRepository.findBySpmallUser_Id(userid, pageable);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("order 가 없어요");
        }
        return orders;
    }

    public List<SpmallOrder> getAllOrder() {
        List<SpmallOrder> orderlist = this.sOrderRepository.findAll();
        return orderlist;
    }

    public SpmallOrder findById(Integer id) {
        Optional<SpmallOrder> sorder = this.sOrderRepository.findById(id);
        if (sorder.isPresent()) {
            return sorder.get();
        }
        throw new DataNotFoundException("order not found");
    }

    public void delete(Integer id) {
        this.sOrderRepository.deleteById(id);
    }

    public void save(SpmallOrder sorder) {
        this.sOrderRepository.save(sorder);
    }

    public List<SpmallOrder> findBySpmallUser_Id(Integer userid) {
        List<SpmallOrder> orders = this.sOrderRepository.findBySpmallUser_Id(userid);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("order 가 없어요");
        }
        return orders;

    }

    public void delete(SpmallOrder order) {
        this.sOrderRepository.delete(order);
    }
}
