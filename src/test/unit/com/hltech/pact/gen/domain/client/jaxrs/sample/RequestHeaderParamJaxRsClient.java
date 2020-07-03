package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class RequestHeaderParamJaxRsClient {

    @GET
    @Path(value = "/testHeadersByParam")
    @Consumes(value = "")
    public Response httpGetWithHeadersByParam(
            @HeaderParam("Header-A") String headerParamA,
            @HeaderParam("Header-B") String headerParamB) {
        return Response.accepted().build();
    }

    @GET
    @Path(value = "/testHeadersByContext")
    @Consumes(value = "")
    public Response httpGetWithHeadersByContext(
            @Context HttpHeaders headerParamA) {
        return Response.accepted().build();
    }
}
