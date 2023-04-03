package planificador;

import ejb.BolsaDAO;
import entidades.Bolsa;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Singleton
public class VencimientoBolsas {
    @Inject
    private BolsaDAO bolsaDAO;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    public void vencerBolsas() {
        System.out.println("\nCorriendo planificador\n");
        LocalDate today = LocalDate.now();
        System.out.println("\nHoy es:" + today + "\n");
        List<Bolsa> bolsas = bolsaDAO.findAllV2();
        System.out.println("Cantidad de bolsas: " + bolsas.size());

        for (int i = 0; i < bolsas.size(); i++) {
            System.out.println("La bolsa es: " + bolsas.get(i).toString());
            System.out.println("\nFecha de la bolsa es:" + bolsas.get(i).getFechaCaducidad() + "\n");

            System.out.println("\nVenciendo la bolsa: " + bolsas.get(i) + "\n");
            bolsas.get(i).setSaldo(0L);
            bolsas.get(i).setPuntosUtilizados(bolsas.get(i).getPuntosAsignados());
            bolsas.get(i).setPuntosAsignados(0L);
            /*
            try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default")) {
                try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
                    entityManager.getTransaction().begin();
                    Bolsa existingBolsa = entityManager.find(Bolsa.class, bolsas.get(i).getId());
                    if (existingBolsa != null) {
                        Field[] fields = bolsas.get(i).getClass().getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            Object value = field.get(bolsas.get(i));
                            if (value != null) {
                                field.set(existingBolsa, value);
                            }
                        }
                        entityManager.merge(existingBolsa);
                    }
                    entityManager.getTransaction().commit();
                    assert existingBolsa != null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            */

            String DB_URL = "jdbc:postgresql://localhost:5433/ppbackend";
            String DB_USER = "postgres";
            String DB_PASSWORD = "postgres";
            String sql = "UPDATE bolsa SET saldo = 0 WHERE id = "+bolsas.get(i).getId();
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Conexión establecida a la base de datos PostgreSQL");

                PreparedStatement stmt = conn.prepareStatement(sql);
                int rowsUpdated = stmt.executeUpdate();
                System.out.println("Se actualizaron " + rowsUpdated + " filas en la tabla personas");

            } catch (SQLException e) {
                System.err.println("Error al conectar a la base de datos PostgreSQL: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Conexión cerrada a la base de datos PostgreSQL");
                    } catch (SQLException e) {
                        System.err.println("Error al cerrar la conexión a la base de datos PostgreSQL: " + e.getMessage());
                    }
                }
            }

        }
    }
}
