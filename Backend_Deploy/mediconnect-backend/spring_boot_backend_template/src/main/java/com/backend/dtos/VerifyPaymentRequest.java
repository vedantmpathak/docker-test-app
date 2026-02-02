package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.PackagePrivate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPaymentRequest {
	
	@NotBlank
	private String razorpay_order_id;
	
	
	@NotBlank
    private String razorpay_payment_id;

    @NotBlank
    private String razorpay_signature;
    @NotBlank
   private Long appointmentId;

}
