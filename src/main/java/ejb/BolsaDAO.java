package ejb;

import entidades.Bolsa;
import entidades.Cliente;
import entidades.ReglaPuntos;
import entidades.ValidezPuntos;
import jakarta.ejb.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;

//TODO se podria meter en un init el EntityManagerFactory
@Stateless
public class BolsaDAO {
    public String getById(Long id) {
        try (Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Bolsa> cq = cb.createQuery(Bolsa.class);
                    Root<Bolsa> rootEntry = cq.from(Bolsa.class);
                    rootEntry.fetch("cliente", JoinType.LEFT);
                    rootEntry.fetch("id_validezPuntos", JoinType.LEFT);
                    cq.where(cb.equal(rootEntry.get("id"), id));
                    CriteriaQuery<Bolsa> all = cq.select(rootEntry);
                    TypedQuery<Bolsa> allQuery = entityManager.createQuery(all);
                    Bolsa bolsa = allQuery.getSingleResult();
                    return jsonb.toJson(bolsa);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String findAll() {
        try (Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true))) {
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Bolsa> cq = cb.createQuery(Bolsa.class);
                    Root<Bolsa> rootEntry = cq.from(Bolsa.class);
                    rootEntry.fetch("cliente", JoinType.LEFT);
                    rootEntry.fetch("id_validezPuntos", JoinType.LEFT);
                    CriteriaQuery<Bolsa> all = cq.select(rootEntry);
                    TypedQuery<Bolsa> allQuery = entityManager.createQuery(all);
                    List<Bolsa> bolsas = allQuery.getResultList();
                    for (Bolsa bolsa : bolsas) {
                        Cliente cliente = bolsa.getCliente();
                        if (cliente != null) {
                            cliente.serialize(); // serialize the cliente object
                        }
                        ValidezPuntos validezPuntos = bolsa.getId_validezPuntos();
                        if (validezPuntos != null) {
                            validezPuntos.serialize(); // serialize the validezPuntos object
                        }
                    }
                    return jsonb.toJson(bolsas);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Long create(Bolsa bolsa)
    {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                bolsa.setFechaAsignacion(new Date());

                List<ReglaPuntos> reglasPuntos = entityManager.createQuery("SELECT r FROM ReglaPuntos r", ReglaPuntos.class).getResultList();
                ReglaPuntos reglaAplicable = null;
                for (ReglaPuntos regla : reglasPuntos) {
                    if (bolsa.getMontoOperacion() >= regla.getLimiteInferior()
                            && bolsa.getMontoOperacion() <= regla.getLimiteSuperior()) {
                        reglaAplicable = regla;
                        break;
                    }
                }

                long puntosAsignados;
                if (reglaAplicable != null) {
                    puntosAsignados = bolsa.getMontoOperacion() / reglaAplicable.getGsPerPoint();
                } else {
                    puntosAsignados = 0L;
                }

                ValidezPuntos validezPuntos = new ValidezPuntos(bolsa.getId_validezPuntos().getFechaInicio(), bolsa.getId_validezPuntos().getFechaFin());
                bolsa.setId_validezPuntos(validezPuntos);
                
                bolsa.setPuntosAsignados(puntosAsignados);
                bolsa.setSaldo(bolsa.getPuntosAsignados());
                bolsa.setPuntosUtilizados(0L);

                entityManager.persist(bolsa);
                entityManager.getTransaction().commit();
            }
        }
        return bolsa.getId();
    }

    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(Bolsa bolsa) {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                entityManager.merge(bolsa);
                entityManager.getTransaction().commit();
            }
        }
        return bolsa.toString();
    }
    //TODO al eliminar una entidad que fue eliminada, luego actualizada, sale un error de "attempt to create delete event with null entity"
    public String delete(Long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Bolsa bolsa = entityManager.find(Bolsa.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(bolsa);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();

        return bolsa.toString();
    }
}

