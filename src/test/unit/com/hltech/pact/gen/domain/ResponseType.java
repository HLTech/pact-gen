package com.hltech.pact.gen.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseType {

    private final String responseFoo;
    private final String responseBar;
    private final int[] intArray;

    public ResponseType(
        String responseFoo, @JsonProperty(defaultValue = "responseReplacedBar") String responseBar, int[] intArray) {
        this.responseFoo = responseFoo;
        this.responseBar = responseBar;
        this.intArray = intArray;
    }

    public String getResponseFoo() {
        return responseFoo;
    }

    public String getResponseBar() {
        return responseBar;
    }

    public int[] getIntArray() {
        return intArray;
    }
}
