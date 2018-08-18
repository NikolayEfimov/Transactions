package services;

import dao.AccountDao;
import dao.TransactionDao;
import model.Account;
import model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private AccountService accountService;
    @Inject
    private TransactionDao transactionDao;
    @Inject
    private AccountDao accountDao;

    @Override
    public synchronized boolean transfer(Transaction transaction) {
        try {
            transactionDao.create(transaction);

            Account from = accountService.accountById(transaction.fromAccountId);
            Account to = accountService.accountById(transaction.toAccountId);

            from.balance =  from.balance.subtract(transaction.amount);
            to.balance  = to.balance.add(transaction.amount);

            accountDao.update(from);
            accountDao.update(to);
            transaction.state = "DONE";
            transactionDao.update(transaction);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;
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
