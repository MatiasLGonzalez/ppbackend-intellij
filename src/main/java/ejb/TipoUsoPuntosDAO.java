package ejb;

import entidades.TipoUsoPuntos;
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

//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class TipoUsoPuntosDAO {
    public String getById(Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TipoUsoPuntos tipoUsoPuntos = entityManager.find(TipoUsoPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(tipoUsoPuntos);
    }
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
    public Long create(TipoUsoPuntos tipoUsoPuntos)
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
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(TipoUsoPuntos tipoUsoPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(tipoUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return tipoUsoPuntos.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
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
