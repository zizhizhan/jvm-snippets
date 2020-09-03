package com.zizhizhan.legacies.thirdparty.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/users/")
public class Users {

    private final Map<String, String> users = new HashMap<String, String>();

    {
        users.put("001", "James");
    }

    @GET
    @Path("/{id}/")
    @Produces("application/json")
    public String user(@PathParam("id") String id) {
        String user = users.get(id);
        if (user == null) {
            user = "Not existing.";
        }
        return user;
    }

    @GET
    public Response add(@PathParam("id") String id, @PathParam("name") String user) {
        users.put(id, user);
        return Response.ok().build();
    }

}
