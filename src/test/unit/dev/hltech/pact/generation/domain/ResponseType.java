package dev.hltech.pact.generation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

class ResponseType {

    private final String responseFoo;
    private final String responseBar;

    public ResponseType(String responseFoo, @JsonProperty(defaultValue = "responseReplacedBar") String responseBar) {
        this.responseFoo = responseFoo;
        this.responseBar = responseBar;
    }

    public String getResponseFoo() {
        return responseFoo;
    }

    public String getResponseBar() {
        return responseBar;
    }
}
