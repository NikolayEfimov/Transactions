package dao;

import model.Transaction;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

import static db.Database.createEntityManager;

@Singleton
public class TransactionDao extends BaseDao {

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
}
