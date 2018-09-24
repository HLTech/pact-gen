package dev.hltech.pact.generation.domain;

public class RequestType {

    private final String requestFoo;
    private final String requestBar;

    public RequestType(String requestFoo, String requestBar) {
        this.requestFoo = requestFoo;
        this.requestBar = requestBar;
    }

    public String getRequestFoo() {
        return requestFoo;
    }

    public String getRequestBar() {
        return requestBar;
    }
}
