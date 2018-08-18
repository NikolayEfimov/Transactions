package db;

import model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class Database {

    private static EntityManagerFactory factory;
    private static final String IN_MEMORY = "h2";

    public static void init() {
        try {
            initAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initAccounts() {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();

        Account account = new Account();
        account.balance = new BigDecimal(100);
        account.name = "Nikolai";

        Account account2 = new Account();
        account2.balance = new BigDecimal(200);;
        account2.name = "Pavel";

        em.persist(account);
        em.persist(account2);
        em.getTransaction().commit();
        em.close();
    }

    public static EntityManager createEntityManager() {
        if (factory == null) factory = Persistence.createEntityManagerFactory(IN_MEMORY);
        return factory.createEntityManager();
    }
}
