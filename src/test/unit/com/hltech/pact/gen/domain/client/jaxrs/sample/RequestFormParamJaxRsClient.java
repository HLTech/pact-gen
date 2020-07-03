package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class RequestFormParamJaxRsClient {

    @GET
    @Path(value = "/testWithFormParameters")
    public Response httpGetWithFromParameters(
            @FormParam("form-a") String matrixParamA,
            @FormParam("form-b") Integer matrixParamB) {
        return Response.accepted().build();
    }
}
