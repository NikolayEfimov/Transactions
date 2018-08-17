package dao;

import model.Account;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

import static db.Database.createEntityManager;

@Singleton
public class AccountDao extends BaseDao{

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

}
