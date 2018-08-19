package services;

import dao.AccountDao;
import exceptions.AccountNotFoundException;
import model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject private AccountDao accountDao;

    @Override
    public List<Account> accounts() {
        return accountDao.accounts();
    }

    @Override
    public Account accountById(Long id) {
        Account account = accountDao.accountById(id);
        if (account == null) throw new AccountNotFoundException("Account with id = " + id + " not found");
        return account;
    }

    @Override
    public Account create(Account account) {
        return (Account) accountDao.create(account);
    }

    @Override
    public void update(Account account) {
        accountDao.update(account);
    }

    @Override
    public void deleteAll() {
        accountDao.deleteAll();
    }

}
