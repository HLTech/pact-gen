package dev.hltech.pact.generation.domain;

public class NestedBrokenType {

    private final BrokenRequestType foo;
    private final String bar;

    public NestedBrokenType(BrokenRequestType foo, String bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public BrokenRequestType getFoo() {
        return foo;
    }

    public String getBar() {
        return bar;
    }
}
