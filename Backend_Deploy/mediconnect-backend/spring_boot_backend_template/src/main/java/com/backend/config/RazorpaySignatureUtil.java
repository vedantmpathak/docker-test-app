package com.backend.config;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RazorpaySignatureUtil {
	
	public static boolean verify(String orderId,String paymentId,String signature,String secret)  throws Exception {
		String payload = orderId + "|" + paymentId;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(),"HmacSHA256"));
		
		byte[] digest = mac.doFinal(payload.getBytes());
		
		String generatedSignature = Base64.getEncoder().encodeToString(digest);
		
		return generatedSignature.equals(signature);
		
		
	}

}
