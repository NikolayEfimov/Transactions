package dao;

import model.Transaction;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

import static db.Database.createEntityManager;

@Singleton
public class TransactionDao {

    public Transaction transactionById(Long id) {
        return createEntityManager().find(Transaction.class, id);
    }

    public List<Transaction> transactions() {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        List transactions = em.createQuery("SELECT t from Transaction t").getResultList();
        em.close();
        return transactions;
    }

    public void create(Transaction transaction) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.persist(transaction);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Transaction transaction) {
        EntityManager em = createEntityManager();
        em.getTransaction().begin();
        em.merge(transaction);
        em.getTransaction().commit();
        em.close();
    }
}
