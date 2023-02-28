package com.tresm.backend.ppbackend;

import entidades.Cliente;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/cliente")
public class ClienteResource {
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
    public String getClientById(@PathParam("id") Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente client = entityManager.find(Cliente.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(client);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public String findAll() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            EntityManager entityManager;
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                entityManager = entityManagerFactory.createEntityManager();
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
                Root<Cliente> rootEntry = cq.from(Cliente.class);
                CriteriaQuery<Cliente> all = cq.select(rootEntry);
                TypedQuery<Cliente> allQuery = entityManager.createQuery(all);
                return jsonb.toJson(allQuery.getResultList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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