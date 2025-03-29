package com.monkcommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monkcommerce.entity.Coupon;
import com.monkcommerce.service.CouponService;

import java.util.List;
import java.util.Map;

@RestController
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.updateCoupon(id, coupon));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Map<String, Object>> getApplicableCoupons(@RequestBody Map<String, Object> cart) {
        return ResponseEntity.ok(couponService.getApplicableCoupons(cart));
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Map<String, Object>> applyCoupon(@PathVariable Long id, @RequestBody Map<String, Object> cart) {
        return ResponseEntity.ok(couponService.applyCoupon(id, cart));
    }
}


