package com.tresm.backend.ppbackend;

import ejb.TipoUsoPuntosDAO;
import entidades.TipoUsoPuntos;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tipoUsoPuntos")
public class TipoUsoPuntosResource {
    @Inject
    private TipoUsoPuntosDAO tipoUsoPuntosDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde tipoUsoPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(tipoUsoPuntosDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(tipoUsoPuntosDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TipoUsoPuntos tipoUsoPuntos)
    {
        return Response.ok(tipoUsoPuntosDAO.create(tipoUsoPuntos)).build();
    }

    @PUT
    public Response update(TipoUsoPuntos tipoUsoPuntos) {
        return Response.ok(tipoUsoPuntosDAO.update(tipoUsoPuntos)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(tipoUsoPuntosDAO.delete(id)).build();
    }
}
