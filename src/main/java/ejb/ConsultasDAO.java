package ejb;

import entidades.Bolsa;
import entidades.CabeceraUsoPuntos;
import entidades.Cliente;
import entidades.TipoUsoPuntos;
import jakarta.ejb.Stateless;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ConsultasDAO {
    public String getUsoDePuntosByConcepto(String cadena) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TipoUsoPuntos> cq = cb.createQuery(TipoUsoPuntos.class);
        Root<TipoUsoPuntos> root = cq.from(TipoUsoPuntos.class);
        Predicate predicate = cb.equal(root.get("descripcion"), cadena);
        cq.where(predicate);
        TypedQuery<TipoUsoPuntos> query = entityManager.createQuery(cq);
        List<TipoUsoPuntos> resultadosTipoUsoPuntos = query.getResultList();


        CriteriaQuery<CabeceraUsoPuntos> cqCabecera = cb.createQuery(CabeceraUsoPuntos.class);
        Root<CabeceraUsoPuntos> rootEntry = cqCabecera.from(CabeceraUsoPuntos.class);
        rootEntry.fetch("cliente", JoinType.LEFT);
        rootEntry.fetch("tipoUsoPuntos", JoinType.LEFT);

        List<CabeceraUsoPuntos> listaCabeceraUsoPuntos = null;
        for(TipoUsoPuntos tipoUsoPuntos: resultadosTipoUsoPuntos){
            cqCabecera.where(cb.equal(rootEntry.get("tipoUsoPuntos"), tipoUsoPuntos));
            CriteriaQuery<CabeceraUsoPuntos> all = cqCabecera.select(rootEntry);
            TypedQuery<CabeceraUsoPuntos> allQuery = entityManager.createQuery(all);
            if (listaCabeceraUsoPuntos == null) {
                listaCabeceraUsoPuntos = allQuery.getResultList();
            }
            else {
                listaCabeceraUsoPuntos.addAll(allQuery.getResultList());
            }
        }
        return jsonb.toJson(listaCabeceraUsoPuntos);
    }

    public String getUsoDePuntosByFecha(String fechaString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = LocalDate.parse(fechaString, formatter);
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CabeceraUsoPuntos> cq = cb.createQuery(CabeceraUsoPuntos.class);
        Root<CabeceraUsoPuntos> root = cq.from(CabeceraUsoPuntos.class);
        root.fetch("cliente", JoinType.LEFT);
        root.fetch("tipoUsoPuntos", JoinType.LEFT);
        Predicate predicate = cb.equal(root.get("fecha"), fecha);
        System.out.println("###" + fecha);
        cq.where(predicate);
        TypedQuery<CabeceraUsoPuntos> query = entityManager.createQuery(cq);
        List<CabeceraUsoPuntos> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
    public String getUsoDePuntosByCliente(String cliente) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CabeceraUsoPuntos> cq = cb.createQuery(CabeceraUsoPuntos.class);
        Root<CabeceraUsoPuntos> root = cq.from(CabeceraUsoPuntos.class);
        root.fetch("cliente", JoinType.LEFT);
        root.fetch("tipoUsoPuntos", JoinType.LEFT);
        Cliente client = entityManager.find(Cliente.class, cliente);
        Predicate predicate = cb.equal(root.get("cliente"), client);
        cq.where(predicate);
        TypedQuery<CabeceraUsoPuntos> query = entityManager.createQuery(cq);
        List<CabeceraUsoPuntos> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
    public String getBolsaByCliente(String cliente) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Bolsa> cq = cb.createQuery(Bolsa.class);
        Root<Bolsa> root = cq.from(Bolsa.class);
        root.fetch("cliente", JoinType.LEFT);
        root.fetch("validezPuntos", JoinType.LEFT);
        Cliente client = entityManager.find(Cliente.class, cliente);
        Predicate predicate = cb.equal(root.get("cliente"), client);
        cq.where(predicate);
        TypedQuery<Bolsa> query = entityManager.createQuery(cq);
        List<Bolsa> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
    public String getClienteByNombre(String nombre) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);
        Predicate predicate = cb.equal(root.get("nombre"), nombre);
        cq.where(predicate);
        TypedQuery<Cliente> query = entityManager.createQuery(cq);
        List<Cliente> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
    public String getClienteByApellido(String apellido) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);
        Predicate predicate = cb.equal(root.get("apellido"), apellido);
        cq.where(predicate);
        TypedQuery<Cliente> query = entityManager.createQuery(cq);
        List<Cliente> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
    public String getClienteByCumple(String fechaString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = LocalDate.parse(fechaString, formatter);
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);
        Predicate predicate = cb.equal(root.get("fechaNacimiento"), fecha);
        cq.where(predicate);
        TypedQuery<Cliente> query = entityManager.createQuery(cq);
        List<Cliente> resultados = query.getResultList();


        return jsonb.toJson(resultados);
    }
}
