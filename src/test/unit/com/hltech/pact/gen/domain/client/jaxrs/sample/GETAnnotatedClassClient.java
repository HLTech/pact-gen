package com.hltech.pact.gen.domain.client.jaxrs.sample;

import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import lombok.Data;
import lombok.Value;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class GETAnnotatedClassClient {

    @GET
    @Path(value = "/testPathGet/{pathParamK}")
    @Consumes(value = "")
    @Produces(value = MediaType.APPLICATION_JSON)
    @InteractionInfo(responseStatus = HttpStatus.OK, responseHeaders = {"key1=val1", "key2=val2"})
    public ExampleDto httpGetResponseAccepted(@HeaderParam("Header-A") String headerParamA,
                                            @HeaderParam("Header-B") String headerParamB,
                                            @DefaultValue (value = "default-value") @QueryParam("Param-Q") String queryParamQ,
                                            @QueryParam("Param-W") Integer queryParamW,
                                            @PathParam("pathParamK") String pathParamK,
                                            String body) {
        return new ExampleDto(UUID.randomUUID().toString(), RandomUtils.nextInt());
    }

    @Value
    public static class ExampleDto {
        String fieldA;
        int fieldB;
    }
}
