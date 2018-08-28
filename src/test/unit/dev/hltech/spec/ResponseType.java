package dev.hltech.spec;

class ResponseType {

    private final String responseFoo;
    private final String responseBar;

    public ResponseType(String responseFoo, String responseBar) {
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
