package com.tresm.backend.ppbackend;

import entidades.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/cliente")
public class ClienteResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde cliente
    """;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String handleClientePost(Cliente client)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return client.toString();
    }
}