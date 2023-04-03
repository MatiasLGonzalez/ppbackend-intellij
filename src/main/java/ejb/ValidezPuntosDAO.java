package ejb;

import entidades.ValidezPuntos;
import jakarta.ejb.Stateless;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.temporal.ChronoUnit;

//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class ValidezPuntosDAO {
    public String getById(Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ValidezPuntos validezPuntos = entityManager.find(ValidezPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(validezPuntos);
    }
    public String findAll() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            EntityManager entityManager;
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                entityManager = entityManagerFactory.createEntityManager();
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<ValidezPuntos> cq = cb.createQuery(ValidezPuntos.class);
                Root<ValidezPuntos> rootEntry = cq.from(ValidezPuntos.class);
                CriteriaQuery<ValidezPuntos> all = cq.select(rootEntry);
                TypedQuery<ValidezPuntos> allQuery = entityManager.createQuery(all);
                return jsonb.toJson(allQuery.getResultList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Long create(ValidezPuntos validezPuntos) throws RuntimeException {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                if (validezPuntos.getDiasDuracion() <= 0)
                    throw new RuntimeException("La duración de los puntos no puede ser 0 o negativo");
                entityManager.persist(validezPuntos);
                entityManager.getTransaction().commit();
            }
        }
        return validezPuntos.getId();
    }
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(ValidezPuntos validezPuntos) {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                if (validezPuntos.getFechaInicio().isAfter(validezPuntos.getFechaFin()) || validezPuntos.getFechaInicio().isEqual(validezPuntos.getFechaFin()))
                    throw new RuntimeException("Fecha Inicio no puede ser igual o mayor a Fecha Fin");
                if (validezPuntos.getDiasDuracion() <= 0)
                    throw new RuntimeException("La duración de los puntos no puede ser 0 o negativo");
                entityManager.merge(validezPuntos);
                entityManager.getTransaction().commit();
            }
        }
        return validezPuntos.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ValidezPuntos validezPuntos = entityManager.find(ValidezPuntos.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(validezPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return validezPuntos.toString();
    }
}
