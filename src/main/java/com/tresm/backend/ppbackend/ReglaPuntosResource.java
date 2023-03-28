package com.tresm.backend.ppbackend;

import ejb.ReglaPuntosDAO;
import entidades.ReglaPuntos;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reglaPuntos")
public class ReglaPuntosResource {
    @Inject
    private ReglaPuntosDAO reglaPuntosDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde reglaPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(reglaPuntosDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(reglaPuntosDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ReglaPuntos reglaPuntos)
    {
        return Response.ok(reglaPuntosDAO.create(reglaPuntos)).build();
    }

    @PUT
    public Response update(ReglaPuntos reglaPuntos) {
        return Response.ok(reglaPuntosDAO.update(reglaPuntos)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(reglaPuntosDAO.delete(id)).build();
    }
}
