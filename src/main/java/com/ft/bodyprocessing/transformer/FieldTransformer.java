package com.ft.bodyprocessing.transformer;

public interface FieldTransformer {

    String transform(String originalField, String transactionId);

}