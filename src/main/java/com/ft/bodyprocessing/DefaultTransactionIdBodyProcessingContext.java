package com.ft.bodyprocessing;

public class DefaultTransactionIdBodyProcessingContext implements TransactionIdBodyProcessingContext {

    private String transactionId;

    public DefaultTransactionIdBodyProcessingContext(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

}
