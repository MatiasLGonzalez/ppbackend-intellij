package com.tresm.backend.ppbackend;

import ejb.BolsaDAO;
import entidades.Bolsa;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bolsa")
public class BolsaResource {
    @Inject
    private BolsaDAO bolsaDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde bolsa
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(bolsaDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(bolsaDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Bolsa bolsa)
    {
        return Response.ok(bolsaDAO.create(bolsa)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Bolsa bolsa) {
        return Response.ok(bolsaDAO.update(bolsa)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(bolsaDAO.delete(id)).build();
    }
}