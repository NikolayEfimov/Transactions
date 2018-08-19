package services;

import dao.TransactionDao;
import model.Account;
import model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static model.Transaction.Status.CANCELED;
import static model.Transaction.Status.DONE;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject AccountService accountService;
    @Inject private TransactionDao transactionDao;

    @Override
    public synchronized void transfer(Transaction tx) {
        try {
            transactionDao.create(tx);

            Account from = accountService.accountById(tx.fromAccountId);

            if (from.balance.compareTo(tx.amount) > -1) {
                Account to = accountService.accountById(tx.toAccountId);
                from.balance = from.balance.subtract(tx.amount);
                to.balance  = to.balance.add(tx.amount);
                accountService.update(from);
                accountService.update(to);
                tx.status = DONE;
            }
            else tx.status = CANCELED;

            transactionDao.update(tx);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            tx.status = CANCELED;
            transactionDao.update(tx);
        }
    }

    @Override
    public List<Transaction> transactions() {
        return transactionDao.transactions();
    }

    @Override
    public Transaction transactionById(Long id) {
        return transactionDao.transactionById(id);
    }
}
