package db;

import model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Database {

    private static EntityManagerFactory factory;
    private static final String IN_MEMORY = "h2";
    private static final String TEST_MODE = "h2-test";

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

        Query query = em.createQuery("SELECT a from Account a");
        System.out.println(query.getResultList().size());

        Account account = new Account();
        account.balance = 100.;
        account.name = "Nikolai";

        Account account2 = new Account();
        account2.balance = 200.;
        account2.name = "Pavel";

        em.persist(account);
        em.persist(account2);
        em.getTransaction().commit();

        System.out.println(query.getResultList().size());
        for (Object o : query.getResultList()) {
            Account acc = (Account)o;
            System.out.println(acc.balance);
        }

        em.close();

        System.out.println(em.isOpen());

    }

    public static EntityManager createEntityManager() {
        if (factory == null) factory = Persistence.createEntityManagerFactory(IN_MEMORY);
        return factory.createEntityManager();
    }
}
