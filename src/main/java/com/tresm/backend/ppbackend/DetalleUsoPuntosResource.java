package com.tresm.backend.ppbackend;

import ejb.DetalleUsoPuntosDAO;
import entidades.DetalleUsoPuntos;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/detalleUsoPuntos")
public class DetalleUsoPuntosResource {
    @Inject
    private DetalleUsoPuntosDAO detalleUsoPuntosDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde detalleUsoPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(detalleUsoPuntosDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(detalleUsoPuntosDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(DetalleUsoPuntos detalleUsoPuntos)
    {
        return Response.ok(detalleUsoPuntosDAO.create(detalleUsoPuntos)).build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(DetalleUsoPuntos detalleUsoPuntos) {
        return Response.ok(detalleUsoPuntosDAO.update(detalleUsoPuntos)).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(detalleUsoPuntosDAO.delete(id)).build();
    }
}
