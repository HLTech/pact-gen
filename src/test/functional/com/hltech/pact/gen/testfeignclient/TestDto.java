package com.hltech.pact.gen.testfeignclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class TestDto {

    private final boolean booleanField;
    private final Boolean booleanClassField;
    private final short shortField;
    private final Short shortClassField;
    private final int intField;
    private final Integer integerClassField;
    private final BigInteger bigIntegerField;
    private final long longField;
    private final Long longClassField;
    private final double doubleField;
    private final Double doubleClassField;
    private final float floatField;
    private final Float floatClassField;
    private final String stringField;
    private final String annotatedStringField;
    private final char charField;
    private final DateTimeDto dateTimeDto;

    public TestDto(
        boolean booleanField,
        Boolean booleanClassField,
        short shortField,
        Short shortClassField,
        int intField,
        Integer integerClassField,
        BigInteger bigIntegerField,
        long longField,
        Long longClassField,
        double doubleField,
        Double doubleClassField,
        float floatField,
        Float floatClassField,
        String stringField,
        @JsonProperty(value = "annotatedStringField", defaultValue = "testDefaultValue") String annotatedStringField,
        char charField,
        DateTimeDto dateTimeDto
    ) {
        this.booleanField = booleanField;
        this.booleanClassField = booleanClassField;
        this.shortField = shortField;
        this.shortClassField = shortClassField;
        this.intField = intField;
        this.integerClassField = integerClassField;
        this.bigIntegerField = bigIntegerField;
        this.longField = longField;
        this.longClassField = longClassField;
        this.doubleField = doubleField;
        this.doubleClassField = doubleClassField;
        this.floatField = floatField;
        this.floatClassField = floatClassField;
        this.stringField = stringField;
        this.annotatedStringField = annotatedStringField;
        this.charField = charField;
        this.dateTimeDto = dateTimeDto;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public Boolean getBooleanClassField() {
        return booleanClassField;
    }

    public short getShortField() {
        return shortField;
    }

    public Short getShortClassField() {
        return shortClassField;
    }

    public int getIntField() {
        return intField;
    }

    public Integer getIntegerClassField() {
        return integerClassField;
    }

    public BigInteger getBigIntegerField() {
        return bigIntegerField;
    }

    public long getLongField() {
        return longField;
    }

    public Long getLongClassField() {
        return longClassField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public Double getDoubleClassField() {
        return doubleClassField;
    }

    public float getFloatField() {
        return floatField;
    }

    public Float getFloatClassField() {
        return floatClassField;
    }

    public String getStringField() {
        return stringField;
    }

    public String getAnnotatedStringField() {
        return annotatedStringField;
    }

    public char getCharField() {
        return charField;
    }

    public DateTimeDto getDateTimeDto() {
        return dateTimeDto;
    }

}
