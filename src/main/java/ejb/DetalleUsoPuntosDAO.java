package ejb;

import entidades.*;
import jakarta.ejb.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.util.List;

//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class DetalleUsoPuntosDAO {
    public String getById(Long id) {
        try (Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<DetalleUsoPuntos> cq = cb.createQuery(DetalleUsoPuntos.class);
                    Root<DetalleUsoPuntos> rootEntry = cq.from(DetalleUsoPuntos.class);
                    rootEntry.fetch("cabeceraUsoPuntos", JoinType.LEFT);
                    rootEntry.fetch("bolsa", JoinType.LEFT);
                    cq.where(cb.equal(rootEntry.get("id"), id));
                    CriteriaQuery<DetalleUsoPuntos> all = cq.select(rootEntry);
                    TypedQuery<DetalleUsoPuntos> allQuery = entityManager.createQuery(all);
                    DetalleUsoPuntos detalleUsoPuntos = allQuery.getSingleResult();

                    List<Bolsa> bolsas = (List)detalleUsoPuntos.getBolsa();
                    for (Bolsa bolsa : bolsas) {
                        bolsa.serialize();
                        Cliente cliente = bolsa.getCliente();
                        if (cliente != null) {
                            cliente.serialize(); // serialize the cliente object
                        }
                        ValidezPuntos validezPuntos = bolsa.getValidezPuntos();
                        if (validezPuntos != null) {
                            validezPuntos.serialize(); // serialize the validezPuntos object
                        }
                    }
                    return jsonb.toJson(detalleUsoPuntos);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String findAll() {
        try (var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            EntityManager entityManager;
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                entityManager = entityManagerFactory.createEntityManager();
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<DetalleUsoPuntos> cq = cb.createQuery(DetalleUsoPuntos.class);
                Root<DetalleUsoPuntos> rootEntry = cq.from(DetalleUsoPuntos.class);
                rootEntry.fetch("bolsa", JoinType.LEFT);
                rootEntry.fetch("cabeceraUsoPuntos", JoinType.LEFT);
                CriteriaQuery<DetalleUsoPuntos> all = cq.select(rootEntry);
                TypedQuery<DetalleUsoPuntos> allQuery = entityManager.createQuery(all);
                return jsonb.toJson(allQuery.getResultList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Long create(DetalleUsoPuntos detalleUsoPuntos)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Bolsa> bolsas = (List)detalleUsoPuntos.getBolsa();
        for (int i = 0; i < bolsas.size(); i++) {
            bolsas.set(i,entityManager.find(Bolsa.class,bolsas.get(i).getId()));
        }
        detalleUsoPuntos.setCabeceraUsoPuntos(entityManager.find(CabeceraUsoPuntos.class, detalleUsoPuntos.getCabeceraUsoPuntos().getId()));
        entityManager.persist(detalleUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return detalleUsoPuntos.getId();
    }
    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(DetalleUsoPuntos detalleUsoPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        if(detalleUsoPuntos.getBolsa() != null){
            List<Bolsa> bolsas = (List)detalleUsoPuntos.getBolsa();
            for (int i = 0; i < bolsas.size(); i++) {
                bolsas.set(i,entityManager.find(Bolsa.class,bolsas.get(i).getId()));
            }
        }
        if(detalleUsoPuntos.getCabeceraUsoPuntos() != null) {
            detalleUsoPuntos.setCabeceraUsoPuntos(entityManager.find(CabeceraUsoPuntos.class, detalleUsoPuntos.getCabeceraUsoPuntos().getId()));
        }
        entityManager.merge(detalleUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return detalleUsoPuntos.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        DetalleUsoPuntos detalleUsoPuntos = entityManager.find(DetalleUsoPuntos.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(detalleUsoPuntos);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return detalleUsoPuntos.toString();
    }
}
