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

import java.lang.reflect.Field;
import java.time.LocalDate;
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
                        ValidezPuntos validezPuntos = bolsa.getValidezPuntos();
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

    public ReglaPuntos buscarReglaAplicable(EntityManager entityManager, Long montoOperacion){
        List<ReglaPuntos> reglasPuntos = entityManager.createQuery("SELECT r FROM ReglaPuntos r", ReglaPuntos.class).getResultList();
        for (ReglaPuntos regla : reglasPuntos) {
            if (montoOperacion >= regla.getLimiteInferior() && montoOperacion <= regla.getLimiteSuperior()) {
                return regla;
            }
        }
        System.out.println("No se encontro ninguna regla que cumpla para bolsa");
        return null;
    }
    public Long create(Bolsa bolsa) {
        System.out.println(bolsa);
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                bolsa.setFechaAsignacion(LocalDate.now());

                ReglaPuntos reglaAplicable = buscarReglaAplicable(entityManager, bolsa.getMontoOperacion());

                long puntosAsignados;
                if (reglaAplicable != null) {
                    puntosAsignados = bolsa.getMontoOperacion() / reglaAplicable.getGsPerPoint();
                } else {
                    puntosAsignados = 0L;
                }

                List<ValidezPuntos> listaValidezPuntos = entityManager.createQuery("SELECT v FROM ValidezPuntos v", ValidezPuntos.class).getResultList();
                ValidezPuntos validezPuntosAplicable = null;
                for (ValidezPuntos regla : listaValidezPuntos) {
                    if ((bolsa.getFechaAsignacion().isAfter(regla.getFechaInicio()) || bolsa.getFechaAsignacion().isEqual(regla.getFechaInicio()))
                            && (bolsa.getFechaAsignacion().isBefore(regla.getFechaFin()) || bolsa.getFechaAsignacion().isEqual(regla.getFechaFin()))) {
                        validezPuntosAplicable = regla;
                        break;
                    }
                }

                bolsa.setValidezPuntos(validezPuntosAplicable);
                System.out.println(validezPuntosAplicable);

                bolsa.setPuntosAsignados(puntosAsignados);
                bolsa.setSaldo(bolsa.getPuntosAsignados());
                bolsa.setPuntosUtilizados(0L);
                bolsa.setFechaCaducidad(bolsa.getFechaAsignacion().plusDays(bolsa.getValidezPuntos().getDiasDuracion()));

                entityManager.persist(bolsa);
                entityManager.getTransaction().commit();
                entityManager.close();
                entityManagerFactory.close();
            }
        }
        return bolsa.getId();
    }

    //TODO verificar si la entidad figura en la bd para modificar, porque si actualizas uno eliminado, vuelve a insertar
    public String update(Bolsa bolsa) {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                entityManager.getTransaction().begin();
                Bolsa existingBolsa = entityManager.find(Bolsa.class, bolsa.getId());
                if (existingBolsa != null) {
                    Field[] fields = bolsa.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object value = field.get(bolsa);
                        if (value != null) {
                            field.set(existingBolsa, value);
                        }
                    }
                    entityManager.merge(existingBolsa);
                }
                entityManager.getTransaction().commit();
                assert existingBolsa != null;
                return existingBolsa.toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
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

