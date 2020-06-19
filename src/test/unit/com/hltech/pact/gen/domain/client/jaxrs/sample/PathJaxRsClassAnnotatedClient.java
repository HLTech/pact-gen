package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(value = "/testPathGet")
public class PathJaxRsClassAnnotatedClient {

    @GET
    public Response httpGetResponseAccepted() {
        return Response.accepted().build();
    }
}
