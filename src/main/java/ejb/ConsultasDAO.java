package ejb;

import entidades.Cliente;
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
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class ConsultasDAO {
    public String getUsoDePuntosByConcepto(TipoUsoPuntos tipoUsoPuntos) {
        var jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TipoUsoPuntos> cq = cb.createQuery(TipoUsoPuntos.class);
        Root<TipoUsoPuntos> root = cq.from(TipoUsoPuntos.class);
        Predicate predicate = cb.equal(root.get("descripcion"), tipoUsoPuntos.getDescripcion());
        cq.where(predicate);
        TypedQuery<TipoUsoPuntos> query = entityManager.createQuery(cq);
        List<TipoUsoPuntos> resultados = query.getResultList();
        return jsonb.toJson(query.getResultList());
    }

}
