package com.tresm.backend.ppbackend;

import entidades.ReglaPuntos;
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

@Path("/reglaPuntos")
public class ReglaPuntosResource {
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde ReglaPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public String getReglaPuntosById(@PathParam("id") Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ReglaPuntos reglaPuntos = entityManager.find(ReglaPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(reglaPuntos);
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
                CriteriaQuery<ReglaPuntos> cq = cb.createQuery(ReglaPuntos.class);
                Root<ReglaPuntos> rootEntry = cq.from(ReglaPuntos.class);
                CriteriaQuery<ReglaPuntos> all = cq.select(rootEntry);
                TypedQuery<ReglaPuntos> allQuery = entityManager.createQuery(all);
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
    public Long handleReglaPuntosPost(ReglaPuntos reglaPuntos)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(reglaPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return reglaPuntos.getId();
    }

    @PUT
    public String updateReglaPuntos(ReglaPuntos reglaPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(reglaPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return reglaPuntos.toString();
    }

    @DELETE
    @Path("/{id}")
    public String deleteReglaPuntos(@PathParam("id") Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ReglaPuntos reglaPuntos = entityManager.find(ReglaPuntos.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(reglaPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return reglaPuntos.toString();
    }
}

