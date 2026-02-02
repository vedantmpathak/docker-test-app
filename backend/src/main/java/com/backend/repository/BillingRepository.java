package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Billing;

public interface BillingRepository extends JpaRepository<Billing, Long> {

	Billing findByRazorpayOrderId(String razorpay_order_id);

}
