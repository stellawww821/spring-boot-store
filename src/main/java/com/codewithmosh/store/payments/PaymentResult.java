package com.codewithmosh.store.payments;

import com.codewithmosh.store.entities.PaymentSatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentSatus paymentStatus;
}
