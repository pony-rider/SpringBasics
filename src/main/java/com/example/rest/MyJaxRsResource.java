package com.example.rest;

import org.springframework.stereotype.Component;

/*import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;*/

@Component
//@Path("/app")
public class MyJaxRsResource {

    MyJaxRsResource(){
        System.out.println(this);
    }

    /*@GET
    @Path("/{type}")
    public String getMessage(@PathParam("type") String type) {
        return "message from JAX-RS app for " + type;
    }*/
}
