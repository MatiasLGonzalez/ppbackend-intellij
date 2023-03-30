package com.tresm.backend.ppbackend;

import ejb.CabeceraUsoPuntosDAO;
import entidades.CabeceraUsoPuntos;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cabeceraUsoPuntos")
public class CabeceraUsoPuntosResource {
    @Inject
    private CabeceraUsoPuntosDAO cabeceraUsoPuntoseDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde cabeceraUsoPuntose
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(cabeceraUsoPuntoseDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(cabeceraUsoPuntoseDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CabeceraUsoPuntos cabeceraUsoPuntos)
    {
        return Response.ok(cabeceraUsoPuntoseDAO.create(cabeceraUsoPuntos)).build();
    }

    @PUT
    public Response update(CabeceraUsoPuntos cabeceraUsoPuntos) {
        return Response.ok(cabeceraUsoPuntoseDAO.update(cabeceraUsoPuntos)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(cabeceraUsoPuntoseDAO.delete(id)).build();
    }
}
