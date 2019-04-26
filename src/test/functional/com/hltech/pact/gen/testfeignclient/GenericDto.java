package com.hltech.pact.gen.testfeignclient;

import java.util.List;

public class GenericDto<T> {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
