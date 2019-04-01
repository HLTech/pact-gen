package com.hltech.pact.gen.domain

import javax.ws.rs.GET
import javax.ws.rs.Path

interface AnotherJaxRsClient {

    @Path("/resource")
    @GET
    SampleDto getResource();
}
