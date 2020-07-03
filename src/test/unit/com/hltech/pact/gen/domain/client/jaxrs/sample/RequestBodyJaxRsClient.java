package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class RequestBodyJaxRsClient {

    @GET
    @Path(value = "/testBody")
    @Consumes(value = "")
    public Response httpGetWithBody(String body) {
        return Response.accepted().build();
    }
}
