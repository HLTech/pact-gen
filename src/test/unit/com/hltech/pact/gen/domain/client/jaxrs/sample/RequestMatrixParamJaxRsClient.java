package com.hltech.pact.gen.domain.client.jaxrs.sample;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class RequestMatrixParamJaxRsClient {

    @GET
    @Path(value = "/testWithMatrixParameters")
    public Response httpGetWithMatrixParameters(
            @MatrixParam("matrix-a") String matrixParamA,
            @MatrixParam("matrix-b") String matrixParamB) {
        return Response.accepted().build();
    }
}
