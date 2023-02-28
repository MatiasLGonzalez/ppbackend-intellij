package com.tresm.backend.ppbackend;

import entidades.Cliente;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public String getClientById(@PathParam("id") Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente client = entityManager.find(Cliente.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(client);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long handleClientePost(Cliente client)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return client.getId();
    }

    @PUT
    public String updateCliente(Cliente client) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(client);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return client.toString();
    }

    @DELETE
    @Path("/{id}")
    public String deleteCliente(@PathParam("id") Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente client = entityManager.find(Cliente.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(client);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return client.toString();
    }
}