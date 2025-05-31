package com.smartavaas.service;

import com.razorpay.RazorpayClient;
import com.razorpay.PaymentLink;
import com.smartavaas.dto.PaymentLinkResponse;
import com.smartavaas.model.RentDetail;
import com.smartavaas.repository.RentDetailRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private RentDetailRepository rentDetailRepository;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void initRazorpayClient() {
        try {
            this.razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Razorpay client", e);
        }
    }

    public PaymentLinkResponse createPaymentLinkForRent(Long rentId) {
        RentDetail rent = rentDetailRepository.findById(rentId).orElseThrow();

        try {
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", new BigDecimal(rent.getAmount()).multiply(BigDecimal.valueOf(100)));
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("callback_url", "https://drab-edyth-kahar12911-2937e591.koyeb.app/api/payment/callback");
            paymentLinkRequest.put("callback_method", "post");
            paymentLinkRequest.put("reference_id", rent.getId().toString());

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            return new PaymentLinkResponse(paymentLink.get("id"), paymentLink.get("short_url"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create payment link", e);
        }
    }


    public void handleWebhook(Map<String, Object> payload, String signature) {
        String webhookSecret = "your_webhook_secret";

        try {
            String payloadStr = new JSONObject(payload).toString();
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] hash = sha256_HMAC.doFinal(payloadStr.getBytes());

            String computedSignature = new String(Base64.getEncoder().encode(hash));

            if (!computedSignature.equals(signature)) {
                throw new SecurityException("Invalid Razorpay webhook signature");
            }

            Map<String, Object> paymentObj = (Map<String, Object>) ((Map<String, Object>) payload.get("payload")).get("payment");
            Map<String, Object> entity = (Map<String, Object>) paymentObj.get("entity");

            String referenceId = (String) entity.get("notes.reference_id");
            if (referenceId != null) {
                Long rentId = Long.parseLong(referenceId);
                RentDetail rent = rentDetailRepository.findById(rentId).orElseThrow();
                rent.setStatus("PAID");
                rentDetailRepository.save(rent);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify webhook signature or update payment", e);
        }
    }
}