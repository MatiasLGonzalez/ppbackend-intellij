package ejb;

import entidades.*;
import jakarta.ejb.*;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.List;

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

    public Long create(Cliente client) {
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

    public void gastarPuntos(Cliente cliente, TipoUsoPuntos tipoUsoPuntos) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager em = entityManagerFactory.createEntityManager();
        System.out.println(cliente);
        System.out.println(tipoUsoPuntos);
        System.out.println(em.createQuery("SELECT SUM(b.saldo) FROM Bolsa b WHERE b.cliente = :cliente", Long.class)
                .setParameter("cliente", cliente)
                .getSingleResult());
        // Calculate the total available points for the given Cliente
        tipoUsoPuntos =  em.find(TipoUsoPuntos.class, tipoUsoPuntos.getId());
        System.out.println(tipoUsoPuntos);
        Long totalPuntosDisponibles = em.createQuery("SELECT SUM(b.saldo) FROM Bolsa b WHERE b.cliente = :cliente", Long.class)
                .setParameter("cliente", cliente)
                .getSingleResult();

        System.out.println("\nPuntos disponibles: " + totalPuntosDisponibles + "\n");
        // Check if the total available points are enough to satisfy the TipoUsoPuntos amount
        if (totalPuntosDisponibles < tipoUsoPuntos.getPuntosRequeridos()) {
            throw new IllegalStateException("El cliente no tiene suficientes puntos disponibles para realizar este uso de puntos.");
        }
        em.getTransaction().begin();
        // Create the CabeceraUsoPuntos entity to record the usage operation
        CabeceraUsoPuntos cabecera = new CabeceraUsoPuntos(cliente, tipoUsoPuntos.getPuntosRequeridos(), LocalDate.now(), tipoUsoPuntos);
        em.persist(cabecera);

        // Get all the Bolsa entities for the given Cliente, sorted by creation date (FIFO)
        List<Bolsa> bolsas = em.createQuery("SELECT b FROM Bolsa b WHERE b.cliente = :cliente ORDER BY b.fechaAsignacion ASC", Bolsa.class)
                .setParameter("cliente", cliente)
                .getResultList();

        // Iterate over the Bolsa entities and deduct the required points from each one until the TipoUsoPuntos amount is satisfied
        Long puntosFaltantes = tipoUsoPuntos.getPuntosRequeridos();
        for (Bolsa bolsa : bolsas) {
            if (puntosFaltantes <= 0) {
                break; // All points have been deducted
            }
            Long puntosDisponibles = bolsa.getSaldo();
            if (puntosDisponibles >= puntosFaltantes) {
                // Deduct the remaining points from this Bolsa and create a DetalleUsoPuntos entity
                bolsa.setSaldo(puntosDisponibles - puntosFaltantes);
                em.merge(bolsa);
                DetalleUsoPuntos detalle = new DetalleUsoPuntos(cabecera, puntosFaltantes, bolsa);
                em.persist(detalle);
                puntosFaltantes = 0L;
            } else {
                // Deduct all the points from this Bolsa and create a DetalleUsoPuntos entity
                bolsa.setSaldo(0L);
                em.merge(bolsa);
                DetalleUsoPuntos detalle = new DetalleUsoPuntos(cabecera, puntosDisponibles, bolsa);
                em.persist(detalle);
                puntosFaltantes -= puntosDisponibles;
            }
        }
        em.getTransaction().commit();
        em.close();
        entityManagerFactory.close();
    }
}
