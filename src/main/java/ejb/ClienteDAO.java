package ejb;

import entidades.Cliente;
import jakarta.ejb.*;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class ClienteDAO {
    public String getById(Long id) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente client = entityManager.find(Cliente.class, id);
        entityManagerFactory.close();
        return jsonb.toJson(client);
    }
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
    public Long create(Cliente client)
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
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(Cliente client) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(client);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return client.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
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
