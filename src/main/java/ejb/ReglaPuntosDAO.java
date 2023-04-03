package ejb;

import entidades.ReglaPuntos;
import jakarta.ejb.Stateless;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class ReglaPuntosDAO {
    public String getById(Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ReglaPuntos reglaPuntos = entityManager.find(ReglaPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(reglaPuntos);
    }
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
    public Long create(ReglaPuntos reglaPuntos)
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
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(ReglaPuntos reglaPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(reglaPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return reglaPuntos.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
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

    public String calculatePuntos(Long monto) {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<ReglaPuntos> reglasPuntos = entityManager.createQuery("SELECT r FROM ReglaPuntos r", ReglaPuntos.class).getResultList();
        ReglaPuntos reglaAplicable = null;
        for (ReglaPuntos regla : reglasPuntos) {
            if (monto >= regla.getLimiteInferior()
                    && monto <= regla.getLimiteSuperior()) {
                reglaAplicable = regla;
                break;
            }
        }

        if( reglaAplicable == null)
            return jsonb.toJson("No hay regla para ese monto.");
        Long dividendo = reglaAplicable.getGsPerPoint();

        entityManagerFactory.close();
        return jsonb.toJson(monto / dividendo);
    }
}
