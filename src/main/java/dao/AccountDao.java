package dao;

import model.Account;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

import static db.Database.createEntityManager;

@Singleton
public class AccountDao {

    public Account accountById(Long id) {
        return createEntityManager().find(Account.class, id);
    }

    public List<Account> accounts() {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        List accounts = em.createQuery("SELECT a from Account a").getResultList();
        em.close();
        return accounts;
    }

    public void save(Account account) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();
        em.close();
    }

    public Account create(Account account) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
        em.close();
        return account;
    }
}
