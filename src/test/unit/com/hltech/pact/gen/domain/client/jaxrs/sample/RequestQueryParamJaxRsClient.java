package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class RequestQueryParamJaxRsClient {

    @GET
    @Path(value = "/testWithQueryParameters")
    @Consumes(value = "")
    public Response httpGetWithQueryParameters(
            @DefaultValue(value = "default-value") @QueryParam("Param-Q") String queryParamQ,
            @QueryParam("Param-W") Integer queryParamW) {
        return Response.accepted().build();
    }
}
