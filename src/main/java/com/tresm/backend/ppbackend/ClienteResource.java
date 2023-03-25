package com.tresm.backend.ppbackend;

import ejb.ClienteDAO;
import entidades.Cliente;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cliente")
public class ClienteResource {
    @Inject
    private ClienteDAO clienteDAO;
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde cliente
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(clienteDAO.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response findAll() {
        return Response.ok(clienteDAO.findAll()).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Cliente client)
    {
        return Response.ok(clienteDAO.create(client)).build();
    }

    @PUT
    public Response update(Cliente client) {
        return Response.ok(clienteDAO.update(client)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok(clienteDAO.delete(id)).build();
    }
}