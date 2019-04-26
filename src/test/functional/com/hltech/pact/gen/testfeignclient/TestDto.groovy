package com.hltech.pact.gen.testfeignclient

class TestDto {

    private final boolean booleanField
    private final Boolean booleanClassField
    private final short shortField
    private final Short shortClassField
    private final int intField
    private final Integer integerClassField
    private final long longField
    private final Long longClassField
    private final double doubleField
    private final Double doubleClassField
    private final float floatField
    private final Float floatClassField
    private final String stringField
    private final char charField
    private final DateTimeDto dateTimeDto

    TestDto(
        boolean booleanField,
        Boolean booleanClassField,
        short shortField,
        Short shortClassField,
        int intField,
        Integer integerClassField,
        long longField,
        Long longClassField,
        double doubleField,
        Double doubleClassField,
        float floatField,
        Float floatClassField,
        String stringField,
        char charField,
        DateTimeDto dateTimeDto
    ) {
        this.booleanField = booleanField
        this.booleanClassField = booleanClassField
        this.shortField = shortField
        this.shortClassField = shortClassField
        this.intField = intField
        this.integerClassField = integerClassField
        this.longField = longField
        this.longClassField = longClassField
        this.doubleField = doubleField
        this.doubleClassField = doubleClassField
        this.floatField = floatField
        this.floatClassField = floatClassField
        this.stringField = stringField
        this.charField = charField
        this.dateTimeDto = dateTimeDto
    }

    boolean getBooleanField() {
        return booleanField
    }

    boolean getBooleanClassField() {
        return booleanClassField
    }

    short getShortField() {
        return shortField
    }

    Short getShortClassField() {
        return shortClassField
    }

    int getIntField() {
        return intField
    }

    Integer getIntegerClassField() {
        return integerClassField
    }

    long getLongField() {
        return longField
    }

    Long getLongClassField() {
        return longClassField
    }

    double getDoubleField() {
        return doubleField
    }

    Double getDoubleClassField() {
        return doubleClassField
    }

    float getFloatField() {
        return floatField
    }

    Float getFloatClassField() {
        return floatClassField
    }

    String getStringField() {
        return stringField
    }

    char getCharField() {
        return charField
    }

    DateTimeDto getDateTimeDto() {
        return dateTimeDto
    }
}
