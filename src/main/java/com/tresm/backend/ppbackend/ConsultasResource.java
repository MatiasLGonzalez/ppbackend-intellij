package com.tresm.backend.ppbackend;

import ejb.ConsultasDAO;
import ejb.TipoUsoPuntosDAO;
import entidades.TipoUsoPuntos;
import jakarta.ejb.PostActivate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/consultas/tipoUsoPuntos")
public class ConsultasResource {
    @Inject
    private ConsultasDAO consultasDAO;

    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
                Hola desde consultas
                """;
    }

    @POST
    @Path("/descripcion")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUsoDePuntosByConcepto(TipoUsoPuntos tipoUsoPuntos)
    {
        return Response.ok(consultasDAO.getUsoDePuntosByConcepto(tipoUsoPuntos)).build();
    }
}
