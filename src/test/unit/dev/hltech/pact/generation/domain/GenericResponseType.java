package dev.hltech.pact.generation.domain;

import java.util.List;

public class GenericResponseType<T> {
    private List<T> data;

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
