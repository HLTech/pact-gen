package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Path;

public interface GETAnnotatedMethodClient {

    @Path("/class")
    String getTestObject();
}
