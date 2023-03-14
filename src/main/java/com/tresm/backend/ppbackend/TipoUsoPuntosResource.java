package com.tresm.backend.ppbackend;

import entidades.TipoUsoPuntos;
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

@Path("/tipoUsoPuntos")
public class TipoUsoPuntosResource {
    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return """
    Hola desde tipoUsoPuntos
    """;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public String getTipoUsoPuntosById(@PathParam("id") Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TipoUsoPuntos tipoUsoPuntos = entityManager.find(TipoUsoPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(tipoUsoPuntos);
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
                CriteriaQuery<TipoUsoPuntos> cq = cb.createQuery(TipoUsoPuntos.class);
                Root<TipoUsoPuntos> rootEntry = cq.from(TipoUsoPuntos.class);
                CriteriaQuery<TipoUsoPuntos> all = cq.select(rootEntry);
                TypedQuery<TipoUsoPuntos> allQuery = entityManager.createQuery(all);
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
    public Long handleTipoUsoPuntosPost(TipoUsoPuntos tipoUsoPuntos)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(tipoUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return tipoUsoPuntos.getId();
    }

    @PUT
    public String updateTipoUsoPuntos(TipoUsoPuntos tipoUsoPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(tipoUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return tipoUsoPuntos.toString();
    }

    @DELETE
    @Path("/{id}")
    public String deleteTipoUsoPuntos(@PathParam("id") Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TipoUsoPuntos tipoUsoPuntos = entityManager.find(TipoUsoPuntos.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(tipoUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return tipoUsoPuntos.toString();
    }
}
