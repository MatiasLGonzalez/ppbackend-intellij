package com.tresm.backend.ppbackend;

import ejb.BolsaDAO;
import ejb.ClienteDAO;
import ejb.ReglaPuntosDAO;
import entidades.Bolsa;
import entidades.Cliente;
import entidades.TipoUsoPuntos;
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
    @Inject
    private ClienteDAO clienteDAO;

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

    @POST
    @Path("/usar-puntos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response usarPuntos(JsonObject jsonData) {
        Cliente cliente = new Cliente(Long.parseLong(jsonData.get("idCliente").toString()));
        TipoUsoPuntos tipoUsoPuntos = new TipoUsoPuntos(Long.parseLong(jsonData.get("idTipoUsoPuntos").toString()));
        clienteDAO.gastarPuntos(cliente, tipoUsoPuntos);
        return Response.ok("Puntos gastados").build();
    }
}