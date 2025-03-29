package com.monkcommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.entity.Coupon;
import com.monkcommerce.repository.CouponRepository;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }
    
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(Long id) {
        Optional<Coupon> coupon = couponRepository.findById(id);
        if (coupon.isPresent()) {
            return coupon.get();
        }
        return null;
    }

    public Coupon updateCoupon(Long id, Coupon coupon) {
        if (couponRepository.existsById(id)) {
            coupon.setId(id);
            return couponRepository.save(coupon);
        }
        return null;
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public Map<String, Object> getApplicableCoupons(Map<String, Object> cart) {
        List<Coupon> coupons = couponRepository.findAll();
        List<Map<String, Object>> applicableCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            double discount = calculateDiscount(coupon, cart);
            if (discount > 0) {
                Map<String, Object> couponDetails = new HashMap<>();
                couponDetails.put("coupon_id", coupon.getId());
                couponDetails.put("type", coupon.getType());
                couponDetails.put("discount", discount);
                applicableCoupons.add(couponDetails);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("applicable_coupons", applicableCoupons);
        return result;
    }

    public Map<String, Object> applyCoupon(Long id, Map<String, Object> cart) {
        Coupon coupon = getCouponById(id);
        if (coupon == null) {
            return null;
        }
        return applyDiscount(coupon, cart);
    }

    private double calculateDiscount(Coupon coupon, Map<String, Object> cart) {
        if (coupon.getType().equals("cart-wise")) {
            return calculateCartWiseDiscount(coupon, cart);
        } else if (coupon.getType().equals("product-wise")) {
            return calculateProductWiseDiscount(coupon, cart);
        } else if (coupon.getType().equals("bxgy")) {
            return calculateBxGyDiscount(coupon, cart);
        }
        return 0.0;
    }

    @SuppressWarnings("unchecked")
	private double calculateCartWiseDiscount(Coupon coupon, Map<String, Object> cart) {
        Map<String, Object> details = objectMapper.convertValue(coupon.getDetails(), Map.class);
        double threshold = ((Number) details.get("threshold")).doubleValue();
        double discountPercent = ((Number) details.get("discount")).doubleValue();
        double cartTotal = calculateCartTotal(cart);
        if (cartTotal > threshold) {
            return (discountPercent / 100) * cartTotal;
        }
        return 0.0;
    }

    @SuppressWarnings("unchecked")
	private double calculateProductWiseDiscount(Coupon coupon, Map<String, Object> cart) {
        Map<String, Object> details = objectMapper.convertValue(coupon.getDetails(), Map.class);
        int productId = ((Number) details.get("product_id")).intValue();
        double discountPercent = ((Number) details.get("discount")).doubleValue();
        List<Map<String, Object>> items = (List<Map<String, Object>>) ((Map<String, Object>) cart.get("cart")).get("items");
        double totalDiscount = 0.0;
        for (Map<String, Object> item : items) {
            if (((Number) item.get("product_id")).intValue() == productId) {
                totalDiscount += (((Number) item.get("price")).doubleValue() * ((Number) item.get("quantity")).intValue()) * (discountPercent / 100);
            }
        }
        return totalDiscount;
    }

    private double calculateBxGyDiscount(Coupon coupon, Map<String, Object> cart) {
        // Implementation of BxGy discount calculation.
        // This requires complex logic to handle buy and get product lists, quantities, and repetition limits.
        // Needs further implementation
        return 0;
    }

    private Map<String, Object> applyDiscount(Coupon coupon, Map<String, Object> cart) {
        if (coupon.getType().equals("cart-wise")) {
            return applyCartWiseDiscount(coupon, cart);
        } else if (coupon.getType().equals("product-wise")) {
            return applyProductWiseDiscount(coupon, cart);
        } else if (coupon.getType().equals("bxgy")) {
            return applyBxGyDiscount(coupon, cart);
        }
        return cart;
    }

    @SuppressWarnings("unchecked")
	private Map<String, Object> applyCartWiseDiscount(Coupon coupon, Map<String, Object> cart) {
        double discount = calculateCartWiseDiscount(coupon, cart);
        double cartTotal = calculateCartTotal(cart);
        double finalPrice = cartTotal - discount;
        Map<String, Object> updatedCart = new HashMap<>(cart);
        Map<String, Object> cartItems = (Map<String, Object>) updatedCart.get("cart");
        cartItems.put("total_price", cartTotal);
        cartItems.put("total_discount", discount);
        cartItems.put("final_price", finalPrice);
        return updatedCart;
    }

    @SuppressWarnings({ "unchecked" })
	private Map<String, Object> applyProductWiseDiscount(Coupon coupon, Map<String, Object> cart) {
        double totalDiscount = calculateProductWiseDiscount(coupon, cart);
        List<Map<String, Object>> items = (List<Map<String, Object>>) ((Map<String, Object>) cart.get("cart")).get("items");
        for (Map<String, Object> item : items) {
            item.put("total_discount", 0);
        }
        double cartTotal = calculateCartTotal(cart);
        double finalPrice = cartTotal - totalDiscount;
        Map<String, Object> updatedCart = new HashMap<>(cart);
        Map<String, Object> cartItems = (Map<String, Object>) updatedCart.get("cart");
        cartItems.put("total_price", cartTotal);
        cartItems.put("total_discount", totalDiscount);
        cartItems.put("final_price", finalPrice);
        return updatedCart;
    }

    private Map<String, Object> applyBxGyDiscount(Coupon coupon, Map<String, Object> cart) {
        // Implementation of applying BxGy discount to cart.
        // Needs further implementation
        return cart;
    }

    @SuppressWarnings("unchecked")
	private double calculateCartTotal(Map<String, Object> cart) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) ((Map<String, Object>) cart.get("cart")).get("items");
        double total = 0.0;
        for (Map<String, Object> item : items) {
            total += ((Number) item.get("price")).doubleValue() * ((Number) item.get("quantity")).intValue();
        }
        return total;
    }
}