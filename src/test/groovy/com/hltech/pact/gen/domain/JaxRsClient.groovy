package com.hltech.pact.gen.domain

import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam

@Path("/base")
interface JaxRsClient {

    @Path("/resource")
    @GET
    SampleDto getResource(@QueryParam("name") String name, @QueryParam("age") int age)

    @Path("/{pathParam}")
    @GET
    SampleDto getData(@PathParam("pathParam") Double pathParam)

    @PUT
    SampleDto getData(SampleDto sampleDto)
}
