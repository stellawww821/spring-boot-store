package com.codewithmosh.store.payments;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.PaymentSatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try{
            var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT) //支付模式：一次性支付（Payment）、订阅支付（SUBSCRIPTION）
                .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(websiteUrl + "/check-cancel")
                .setPaymentIntentData(createPaymentIntent(order));

            order.getItems().forEach(item -> {
                var lineItem = createLieItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build()) ;
            return new CheckoutSession(session.getUrl());
        }
        catch(StripeException ex){
            System.out.println(ex.getMessage());
            throw new PaymentException();
        }
    }

    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder().putMetadata("order_id", order.getId().toString()).build();
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentSatus.PAID));
                case "payment_intent.payment_failed" ->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentSatus.FAILED));
                default ->
                    Optional.empty();
            };
        }
        catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Stripe Signature");
        }
    }

    private Long extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
            () ->new PaymentException("Could not deserialize Stripe event.Check the SDK and API version.")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private static SessionCreateParams.LineItem createLieItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item)).build();
    }

    private static SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(
                        item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
