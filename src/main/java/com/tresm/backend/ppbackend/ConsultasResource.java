package com.tresm.backend.ppbackend;

import ejb.ConsultasDAO;
import ejb.TipoUsoPuntosDAO;
import entidades.TipoUsoPuntos;
import jakarta.ejb.PostActivate;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/consultas")
public class ConsultasResource {
    @Inject
    private ConsultasDAO consultasDAO;

    @GET
    @Path("/tipoUsoPuntos/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
                Hola desde consultas
                """;
    }

    @Path("/tipoUsoPuntos/descripcion")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getCabeceraUsoPuntosByDescripcion(@QueryParam("cadena") String cadena) {
        return Response.ok(consultasDAO.getUsoDePuntosByConcepto(cadena)).build();
    }
    @Path("/tipoUsoPuntos/fecha")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getCabeceraUsoPuntosByFecha(@QueryParam("cadena") String fechaString) {
        return Response.ok(consultasDAO.getUsoDePuntosByFecha(fechaString)).build();
    }
    @Path("/tipoUsoPuntos/cliente")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getCabeceraUsoPuntosByCliente(@QueryParam("cadena") String cliente) {
        return Response.ok(consultasDAO.getUsoDePuntosByCliente(cliente)).build();
    }
    @Path("/bolsa/cliente")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getBolsaByCliente(@QueryParam("cadena") String cliente) {
        return Response.ok(consultasDAO.getBolsaByCliente(cliente)).build();
    }
    @Path("/cliente/nombre")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getClientByNombre(@QueryParam("cadena") String nombre) {
        return Response.ok(consultasDAO.getClienteByNombre(nombre)).build();
    }
    @Path("/cliente/apellido")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getClienteByNombre(@QueryParam("cadena") String apellido) {
        return Response.ok(consultasDAO.getClienteByApellido(apellido)).build();
    }
    @Path("/cliente/cumple")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getClienteByCumple(@QueryParam("cadena") String fechaString) {
        return Response.ok(consultasDAO.getClienteByCumple(fechaString)).build();
    }
    @Path("/bolsa/rango")
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public Response getBolsaByRango(@QueryParam("inferior") Long inferior, @QueryParam("superior") Long superior) {
        return Response.ok(consultasDAO.getBolsaByRango(inferior, superior)).build();
    }
}
