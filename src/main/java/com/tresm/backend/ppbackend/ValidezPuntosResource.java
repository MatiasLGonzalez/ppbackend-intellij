package com.tresm.backend.ppbackend;

import ejb.ValidezPuntosDAO;
import entidades.ValidezPuntos;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/validezPuntos")
public class ValidezPuntosResource {
    @Inject
    private ValidezPuntosDAO validezPuntosDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde validezPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(validezPuntosDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(validezPuntosDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ValidezPuntos validezPuntos)
    {
        return Response.ok(validezPuntosDAO.create(validezPuntos)).build();
    }

    @PUT
    public Response update(ValidezPuntos validezPuntos) {
        return Response.ok(validezPuntosDAO.update(validezPuntos)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(validezPuntosDAO.delete(id)).build();
    }
}
