package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class RequestPathParamJaxRsClient {

    @GET
    @Path(value = "/testWithPathParameters/{pathParamK}")
    @Consumes(value = "")
    public Response httpGetWithPathParameters(@PathParam("pathParamK") String pathParamK) {
        return Response.accepted().build();
    }
}
