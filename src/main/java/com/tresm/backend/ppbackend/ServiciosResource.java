package com.tresm.backend.ppbackend;

import ejb.BolsaDAO;
import ejb.ReglaPuntosDAO;
import entidades.Bolsa;
import entidades.ValidezPuntos;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/servicios")
public class ServiciosResource {

    @Inject
    private BolsaDAO bolsaDAO;
    @Inject
    private ReglaPuntosDAO reglaPuntosDAO;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @POST
    @Path("/cargar-puntos")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cargarPuntos(Bolsa bolsa) {
        return Response.ok(bolsaDAO.create(bolsa)).build();
    }

    @GET
    @Path("/consulta-cant-puntos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarPuntosXMonto(JsonObject jsonData) {
        Long monto = Long.parseLong(jsonData.get("monto").toString());

        return Response.ok(reglaPuntosDAO.calculatePuntos(monto)).build();
    }
}