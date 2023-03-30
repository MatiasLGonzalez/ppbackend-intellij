package ejb;

import entidades.CabeceraUsoPuntos;
import entidades.Cliente;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class CabeceraUsoPuntosDAO {
    public String getById(Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CabeceraUsoPuntos cabeceraUsoPuntos = entityManager.find(CabeceraUsoPuntos.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(cabeceraUsoPuntos);
    }
    public String findAll() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            EntityManager entityManager;
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                entityManager = entityManagerFactory.createEntityManager();
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<CabeceraUsoPuntos> cq = cb.createQuery(CabeceraUsoPuntos.class);
                Root<CabeceraUsoPuntos> rootEntry = cq.from(CabeceraUsoPuntos.class);
                CriteriaQuery<CabeceraUsoPuntos> all = cq.select(rootEntry);
                TypedQuery<CabeceraUsoPuntos> allQuery = entityManager.createQuery(all);
                return jsonb.toJson(allQuery.getResultList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Long create(CabeceraUsoPuntos cabeceraUsoPuntos)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        cabeceraUsoPuntos.setCliente(entityManager.find(Cliente.class, cabeceraUsoPuntos.getCliente().getId()));
        entityManager.persist(cabeceraUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return cabeceraUsoPuntos.getId();
    }
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(CabeceraUsoPuntos cabeceraUsoPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(cabeceraUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return cabeceraUsoPuntos.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CabeceraUsoPuntos cabeceraUsoPuntos = entityManager.find(CabeceraUsoPuntos.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(cabeceraUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return cabeceraUsoPuntos.toString();
    }
}
