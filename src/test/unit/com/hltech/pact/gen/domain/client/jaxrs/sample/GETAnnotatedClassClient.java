package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class GETAnnotatedClassClient {

    @GET
    @Path(value = "/testPathGet/{pathParamK}")
    @Consumes(value = "")
    public Response httpGetResponseAccepted(
            @HeaderParam("Header-A") String headerParamA,
            @HeaderParam("Header-B") String headerParamB,
            @DefaultValue(value = "default-value") @QueryParam("Param-Q") String queryParamQ,
            @QueryParam("Param-W") Integer queryParamW,
            @PathParam("pathParamK") String pathParamK,
            String body) {
        return Response.accepted().build();
    }
}
